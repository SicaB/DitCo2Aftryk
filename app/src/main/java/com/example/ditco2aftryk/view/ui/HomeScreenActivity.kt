package com.example.ditco2aftryk.view.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.model.entities.DailyCo2Count
import com.example.ditco2aftryk.utils.AlarmReceiver
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.activity_home_screen.headline
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenActivity : AppCompatActivity(), Listener, OnChartValueSelectedListener {

    private val yValues = ArrayList<Entry>()
    private var set1: LineDataSet? = null
    private var alarmMgr: AlarmManager? = null

    // week days for the diagram
    private var weekDays = arrayOf("man.", "tirs.", "ons.", "tors.", "fre.", "lør.", "søn,")

    // In LineChart and progressbar arrays. 1, 2, 3, 4, 5, 6, 7 = count of days in the week (Monday-Sunday)
    private var countTodayForLineChart: Float = 0f
    private var countTodayForProgressbar: Int = 0
    private var listOfWeekCountsForLineChart = ArrayList<Float>()
    private var listOfWeekCountsForProgressbar = ArrayList<Int>()

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var lineChart: LineChart
    private lateinit var alarmIntent: PendingIntent
    private lateinit var inputIntoWeeklyTable: DailyCo2Count
    private var now = Calendar.getInstance()
    var context = this

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd/M/yyyy")
    val currentDate = sdf.format(Date())!!

    companion object {

        lateinit var appContext: Context

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this activity to the layout xml file using databinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        // create the viewModel
        viewModel = ViewModelProviders.of(this).get(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        viewModel.listener = this
        appContext = applicationContext

        // Initialiserer linechart
        lineChart = findViewById(R.id.line_chart)

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // start next activity
            startActivity(Intent(this, EnterCo2Activity::class.java))
        }

        // check if date of last input in todaysCountTable = today. If not -> delete todays count
        val dateInCo2CountTable: String? = viewModel.dateInCo2CountTable
        viewModel.deleteInputsOlderThanOneWeek()
        if (dateInCo2CountTable != null && dateInCo2CountTable != currentDate){
            viewModel.deleteCountDay()
        }
        viewModel.insertEmptyValuesIntoWeekTable()

        // Create the observer which updates the UI with todays count
        val co2CountObserver = Observer<String> { newCount ->
            if (newCount == null) {
                co2counter.setText(R.string.defaultValue)
                checkWeekdayAndUpdateLineChart(0f)
                Log.d("TodayObserver", "observer1: nothing saved in todays database")
            } else {
                // Update UI with current data
                val co2CounterToday = newCount.toFloat() / 1000
                countTodayForLineChart = co2CounterToday
                co2counter.text = (String.format("%.2f", co2CounterToday) + " kg")
                checkWeekdayAndUpdateLineChart(co2CounterToday)

                val circleProcess = newCount.toFloat().toInt()
                circle.progress = circleProcess
                countTodayForProgressbar = circleProcess
                Log.d("TodayObserver", "observer1: new count for today $countTodayForProgressbar")

                // Check weekday today and insert accumulated count in the weekly database table
                when (now.get(Calendar.DAY_OF_WEEK)){
                    Calendar.MONDAY -> inputIntoWeeklyTable = DailyCo2Count(1, newCount, currentDate)
                    Calendar.TUESDAY -> inputIntoWeeklyTable = DailyCo2Count(2, newCount, currentDate)
                    Calendar.WEDNESDAY -> inputIntoWeeklyTable = DailyCo2Count(3, newCount, currentDate)
                    Calendar.THURSDAY -> inputIntoWeeklyTable = DailyCo2Count(4, newCount, currentDate)
                    Calendar.FRIDAY -> inputIntoWeeklyTable = DailyCo2Count(5, newCount, currentDate)
                    Calendar.SATURDAY -> inputIntoWeeklyTable = DailyCo2Count(6, newCount, currentDate)
                    Calendar.SUNDAY -> inputIntoWeeklyTable = DailyCo2Count(7, newCount, currentDate)
                }
                viewModel.insertDailyCount(inputIntoWeeklyTable)

            }
        }

        // Create the observer which updates the progressbar and linechart for weekdays
        val weeklyCo2CountObserver = Observer<List<String>> { Count ->
            if (Count != null) {
                for (i in 0..6){
                    listOfWeekCountsForProgressbar[i] = Count[i].toFloat().toInt()
                    listOfWeekCountsForLineChart[i] = Count[i].toFloat()/1000
                    setLineChart(listOfWeekCountsForLineChart)
                }
                Log.d("weekObserver", "observer2: new count for weekday $listOfWeekCountsForLineChart")
            }
        }

        // Observe the LiveData, passing in this activity as the LifeCycleOwner and the observer.
        viewModel.accumulatedCo2Count.observe(this, co2CountObserver)
        viewModel.changesToWeeklyCounts.observe(this, weeklyCo2CountObserver)

        // Add default values to progressbar and linechart array
        for (i in 0..6){
            listOfWeekCountsForProgressbar.add(i,0)
            listOfWeekCountsForLineChart.add(i, 0f)
        }


        // Add default values to linechart
        yValues.add(Entry(0f, 0f, "0"))
        yValues.add(Entry(1f, 0f, "1"))
        yValues.add(Entry(2f, 0f, "2"))
        yValues.add(Entry(3f, 0f, "3"))
        yValues.add(Entry(4f, 0f, "4"))
        yValues.add(Entry(5f, 0f, "5"))
        yValues.add(Entry(6f, 0f, "6"))

        // Set customization and preferences
        set1 = LineDataSet(yValues, "DataSet 1")
        set1?.setCircleColor(R.color.colorBlack)
        set1?.color = ContextCompat.getColor(this, R.color.colorButtonLightGreen)
        set1?.setCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
        set1?.lineWidth = 5f
        set1?.circleRadius = 5f
        set1?.valueTextSize = 0f
        set1?.setDrawFilled(false)
        set1?.setDrawHighlightIndicators(false)
        set1?.isHighlightEnabled = true
        set1?.mode = LineDataSet.Mode.CUBIC_BEZIER


        lineChart.setOnChartValueSelectedListener(this)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1!!)
        val data = LineData(dataSets)

        // set data
        lineChart.data = data
        lineChart.description?.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.setScaleEnabled(false)

        // position of x-axis and counts of labels
        lineChart.xAxis.labelCount = 5
        lineChart.xAxis.position = XAxis.XAxisPosition.TOP

        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(true)
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.axisMaximum = 46f
        lineChart.axisLeft.axisMaximum = 46f
        lineChart.axisRight.axisMinimum = -2f
        lineChart.axisLeft.axisMinimum = -2f

        // Text customization
        lineChart.xAxis.textColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart.xAxis.textSize = 12f

        // Set the weekdays to the x-axis
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(weekDays)

        // Grid customization
        lineChart.xAxis.enableAxisLineDashedLine(8f, 18f, 0f)
        lineChart.xAxis.gridColor = ContextCompat.getColor(this, R.color.colorBlack)
        lineChart.xAxis.gridLineWidth = 2f
        lineChart.xAxis.axisLineColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart.xAxis.axisLineWidth = 2f


        // Alarm manager.
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            //intent.putExtra("accumulatedCountForTheDay", viewModel.accumulatedCo2Count)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        //make sure you aren't setting alarm for earlier today
        checkTime(calendar)

        alarmMgr?.setExact(
            AlarmManager.RTC,
            calendar.timeInMillis,
            alarmIntent
        )

    }

    // Function to update todays count in LineChart
    private fun checkWeekdayAndUpdateLineChart(value: Float){
        when (now.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(1, value.toString(), currentDate)
                weekDays[0] = "Idag"
            }
            Calendar.TUESDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(2, value.toString(), currentDate)
                weekDays[1] = "Idag"
            }
            Calendar.WEDNESDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(3, value.toString(), currentDate)
                weekDays[2] = "Idag"
            }
            Calendar.THURSDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(4, value.toString(), currentDate)
                weekDays[3] = "Idag"
            }
            Calendar.FRIDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(5, value.toString(), currentDate)
                weekDays[4] = "Idag"
            }
            Calendar.SATURDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(6, value.toString(), currentDate)
                weekDays[5] = "Idag"
            }
            Calendar.SUNDAY -> {
//                yValues[6] = Entry(6f, value, "6")
//                listOfWeekCountsForLineChart[6] = value
                inputIntoWeeklyTable = DailyCo2Count(7, value.toString(), currentDate)
                weekDays[6] = "Idag"
            }
        }
        viewModel.insertDailyCount(inputIntoWeeklyTable)
        lineChart.invalidate()
    }

    // Function to set weekly counts to LineChart
    private fun setLineChart(values: ArrayList<Float>){
        yValues[0] = Entry(0f, values[0], "0")
        yValues[1] = Entry(1f, values[1], "1")
        yValues[2] = Entry(2f, values[2], "2")
        yValues[3] = Entry(3f, values[3], "3")
        yValues[4] = Entry(4f, values[4], "4")
        yValues[5] = Entry(5f, values[5], "5")
        yValues[6] = Entry(6f, values[6], "6")
        lineChart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.item1) {
            binding.viewmodel?.deleteCountDay()
            resetInputToday()
            return true
        }
        if (id == R.id.item2) {
            binding.viewmodel?.deleteCountDay()
            binding.viewmodel?.deleteCountWeek()
            resetInputsAllWeek()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    // ensure that we don't set reminder for the past
    private fun checkTime(reminder: Calendar) {
        val now = Calendar.getInstance()
        if (reminder.before(now)) {
            val alarmForFollowingDay = reminder.timeInMillis + 86400000L
            reminder.timeInMillis = alarmForFollowingDay
        }
    }

    // Function to reset inputs for all week
    private fun resetInputsAllWeek(){
        yValues[0] = Entry(0f, 0f, "0")
        yValues[1] = Entry(1f, 0f, "1")
        yValues[2] = Entry(2f, 0f, "2")
        yValues[3] = Entry(3f, 0f, "3")
        yValues[4] = Entry(4f, 0f, "4")
        yValues[5] = Entry(5f, 0f, "5")
        yValues[6] = Entry(6f, 0f, "6")

        for (i in 0..6){
            listOfWeekCountsForProgressbar[i] = 0
            listOfWeekCountsForLineChart[i] = 0f
        }

        lineChart.invalidate()

    }

    // Function to reset input for today
    private fun resetInputToday(){
        countTodayForLineChart = 0f
        countTodayForProgressbar = 0
        circle.progress = 0
        checkWeekdayAndUpdateLineChart(0f)
    }

    override fun onNothingSelected() {
    }

    // Function to update LineChart with correct values when weekday in LineChart is selected
    override fun onValueSelected(e: Entry?, h: Highlight?) {

        if (e != null) {
            val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
            when(e.data) {
                "0" -> {
                    if (dayOfWeek == 2) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.monday)
                        if (listOfWeekCountsForProgressbar[0] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[0]
                            yValues[0] = Entry(0f, listOfWeekCountsForLineChart[0], "0")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[0]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }

                "1" -> {
                    if (dayOfWeek == 3) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.tuesday)
                        if (listOfWeekCountsForProgressbar[1] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[1]
                            yValues[1] = Entry(1f, listOfWeekCountsForLineChart[1], "1")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[1]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
                "2" -> {
                    if (dayOfWeek == 4) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.wednesday)
                        if (listOfWeekCountsForProgressbar[2] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[2]
                            yValues[2] = Entry(2f, listOfWeekCountsForLineChart[2], "2")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[2]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
                "3" -> {
                    if (dayOfWeek == 5) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.thursday)
                        if (listOfWeekCountsForProgressbar[3] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[3]
                            yValues[3] = Entry(3f, listOfWeekCountsForLineChart[3], "3")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[3]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
                "4" -> {
                    if (dayOfWeek == 6) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.friday)
                        if (listOfWeekCountsForProgressbar[4] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[4]
                            yValues[4] = Entry(4f, listOfWeekCountsForLineChart[4], "4")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[4]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
                "5" -> {
                    if (dayOfWeek == 7) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.saturday)
                        if (listOfWeekCountsForProgressbar[5] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[5]
                            yValues[5] = Entry(5f, listOfWeekCountsForLineChart[5], "5")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[5]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
                "6" -> {
                    if (dayOfWeek == 1) {
                        headline.setText(R.string.today)
                        if (countTodayForProgressbar != 0){
                            circle?.progress = countTodayForProgressbar
                            co2counter.text = (String.format("%.2f", countTodayForLineChart) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }

                    } else {
                        headline.setText(R.string.sunday)
                        if (listOfWeekCountsForProgressbar[6] != 0){
                            circle?.progress = listOfWeekCountsForProgressbar[6]
                            yValues[6] = Entry(6f, listOfWeekCountsForLineChart[6], "6")
                            co2counter.text = (String.format("%.2f", listOfWeekCountsForLineChart[6]) + " kg")
                        } else {
                            circle?.progress = 0
                            co2counter.setText(R.string.defaultValue)
                        }
                    }
                }
            }
        }
    }

    override fun onSuccess() {
    }

    override fun onFailure(message: String) {
    }

}
