package com.example.ditco2aftryk.view.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.model.AlarmService
import com.example.ditco2aftryk.utils.AlarmReceiver
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_flight.*
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.activity_home_screen.headline
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenActivity : AppCompatActivity(), Listener, OnChartValueSelectedListener {

    private val yValues = ArrayList<Entry>()
    private var set1: LineDataSet? = null
    private var yesterdaysCountForProgressbar: Int? = null
    private var countTodayForProgressbar: Int? = null
    private var alarmMgr: AlarmManager? = null
    private var yesterdaysCountForLinechart: Float? = null
    private var todaysCountForLinechart: Float? = null
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var lineChart: LineChart
    private lateinit var alarmIntent: PendingIntent
    var context = this

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

        // Create the observer which updates the UI
        val co2CountObserver = Observer<String> { newCount ->
            if (newCount == null) {
                co2counter.text = "0.00 kg"
                circle?.progress = 0
                todaysCountForLinechart = 0f
                lineChart.invalidate()
            } else {
                // Update UI with current data
                val co2CounterToday = newCount.toFloat() / 1000
                todaysCountForLinechart = co2CounterToday
                co2counter.text = (String.format("%.2f", co2CounterToday) + " kg")
                yValues[6] = Entry(6f, co2CounterToday, "6")
                lineChart.invalidate()

                val circleProcess = newCount.toInt()
                circle.progress = circleProcess
                countTodayForProgressbar = circleProcess
                Log.d("alarmmanager", "observer1: $countTodayForProgressbar")

            }
        }

        // Create the observer which updates the UI
        val dailyCo2CountObserver = Observer<String> {newCount2 ->
            if (newCount2 == null) {
                yesterdaysCountForLinechart = 0f
                yesterdaysCountForProgressbar = 0
                lineChart.invalidate()
            } else {
                val dailyCo2Counter = newCount2.toFloat()/1000
//                dailyCo2counter.text = (String.format("%.2f", dailyCo2Counter) + " kg")
                yesterdaysCountForProgressbar = newCount2.toInt()
                yesterdaysCountForLinechart = dailyCo2Counter
                yValues[5] = Entry(5f, dailyCo2Counter, "5")
                lineChart.invalidate()
                Log.d("alarmmanager", "observer2: $dailyCo2Counter")
            }
        }

        // Observe the LiveData, passing in this activity as the LifeCycleOwner and the observer.
        viewModel.accumulatedCo2Count.observe(this, co2CountObserver)
        viewModel.dailyCo2Count.observe(this, dailyCo2CountObserver)

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // start next activity
            startActivity(Intent(this, EnterCo2Activity::class.java))
        }



        if (countTodayForProgressbar == null){
            yValues.add(Entry(0f, 0f, "0"))
            yValues.add(Entry(1f, 0f, "1"))
            yValues.add(Entry(2f, 0f, "2"))
            yValues.add(Entry(3f, 0f, "3"))
            yValues.add(Entry(4f, 0f, "4"))
            yValues.add(Entry(5f, 0f, "5"))
            yValues.add(Entry(6f, 0f, "6"))
        }

        set1 = LineDataSet(yValues, "DataSet 1")
        set1?.setCircleColor(R.color.colorBlack)
        // Set preferences
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
        lineChart.axisRight.axisMinimum = 0f
        lineChart.axisLeft.axisMinimum = 0f

        // Text customization
        lineChart.xAxis.textColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart.xAxis.textSize = 12f

        // Add week days to the diagram
        val weekDays = ArrayList<String>()
        weekDays.add("man.")
        weekDays.add("tirs.")
        weekDays.add("ons.")
        weekDays.add("tors.")
        weekDays.add("fre.")
        weekDays.add("igår")
        weekDays.add("idag")

        // Set the weekdays to the x-axis
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(weekDays)

        // Grid customization
//        lineChart?.axisRight?.enableGridDashedLine(5f, 5f, 0f)
//        lineChart?.axisLeft?.enableGridDashedLine(5f, 5f, 0f)
//        lineChart?.xAxis?.enableGridDashedLine(5f, 5f, 0f)
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
            set(Calendar.SECOND, 1)
        }

        //make sure you aren't setting alarm for earlier today
        checkTime(calendar)

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
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
            countTodayForProgressbar = 0
            todaysCountForLinechart = 0f
            yValues[6] = Entry(6f, 0f, "6")
            lineChart.invalidate()
            return true
        }
        if (id == R.id.item2) {
            binding.viewmodel?.deleteCountDay()
            binding.viewmodel?.deleteCountWeek()
            countTodayForProgressbar = 0
            todaysCountForLinechart = 0f
            yesterdaysCountForProgressbar = 0
            yesterdaysCountForLinechart = 0f
            yValues[5] = Entry(5f, 0f, "5")
            yValues[6] = Entry(6f, 0f, "6")
            lineChart.invalidate()
            return true
        }
        if (id == R.id.item3) {
            binding.viewmodel?.insertDailyCount()
            countTodayForProgressbar = 0
            todaysCountForLinechart = 0f
            yValues[6] = Entry(6f, 0f, "6")
            lineChart.invalidate()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    //ensure that we don't set reminder for the past
    private fun checkTime(reminder: Calendar) {
        val now = Calendar.getInstance()
        if (reminder.before(now)) {
            val alarmForFollowingDay = reminder.timeInMillis + 86400000L
            reminder.timeInMillis = alarmForFollowingDay
        }
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        if (e != null) {
            when(e.data) {
                "6" -> {
                    headline.text = "Idag"
                    if (countTodayForProgressbar != null) {
                        circle?.progress = countTodayForProgressbar!!
                        co2counter.text = (String.format("%.2f", todaysCountForLinechart) + " kg")
                    } else {
                        circle?.progress = 0
                        co2counter.text = "0.00 kg"
                    }
                }
                "5" -> {
                    headline.text = "Igår"
                    if (yesterdaysCountForProgressbar != null) {
                        circle?.progress = yesterdaysCountForProgressbar!!
                        co2counter.text = (String.format("%.2f", yesterdaysCountForLinechart) + " kg")
                    } else {
                        circle?.progress = 0
                        co2counter.text = "0.00 kg"
                    }
                }
                else -> toast("not implemented yet!")
            }
        }
    }

    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
