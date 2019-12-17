package com.example.ditco2aftryk.view.ui

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.android.synthetic.main.fragment_fragment_one.*
import kotlinx.android.synthetic.main.fragment_fragment_two.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList


class HomeScreenActivity : AppCompatActivity(), Listener, OnChartValueSelectedListener {

    private val yValues = ArrayList<Entry>()
    private val yValueHighlight = ArrayList<Entry>()
    private var set1: LineDataSet? = null
    private var set2: LineDataSet? = null
    private var alarmMgr: AlarmManager? = null

    // week days for the diagram
    private var weekDays = arrayOf("Man.", "Tirs.", "Ons.", "Tors.", "Fre.", "Lør.", "Søn.")

    // In LineChart and progressbar arrays. 1, 2, 3, 4, 5, 6, 7 = count of days in the week (Monday-Sunday)
    private var countTodayForLineChart: Float = 0f
    private var countTodayForProgressbar: Int = 0
    private var listOfWeekCountsForLineChart = ArrayList<Float>()
    private var listOfWeekCountsForProgressbar = ArrayList<Int>()
    private var dataSets = ArrayList<ILineDataSet>()
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var lineChart: LineChart
    private lateinit var alarmIntent: PendingIntent
    private lateinit var inputIntoWeeklyTable: DailyCo2Count
    private lateinit var dialogInfo: Dialog
    private lateinit var dialogAverageReached: Dialog
    private lateinit var dialogMaxEntered: Dialog
    private lateinit var button: Button
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
        viewModel = ViewModelProvider(this).get(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        viewModel.listener = this
        appContext = applicationContext

        // Initialiserer linechart
        lineChart = findViewById(R.id.line_chart)

        // Actionbar icon
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // start next activity
            startActivity(Intent(this, EnterCo2Activity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Setup viewPager adapter
        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FragmentOne())
        adapter.addFragment(FragmentTwo())
        viewPager.adapter = adapter
        viewPager.currentItem = 2

        // check if date of last input in todaysCountTable = today. If not -> delete todays count
        val dateInCo2CountTable: String? = viewModel.dateInCo2CountTable
        viewModel.deleteInputsOlderThanOneWeek()
        if (dateInCo2CountTable != null && dateInCo2CountTable != currentDate){
            viewModel.deleteCountDay()
        }

        // Insert empty values in table if nothing is inserted
        viewModel.insertEmptyValuesIntoWeekTable()

        // Create the observer which updates the UI with todays count
        val co2CountObserver = Observer<String> { newCount ->
            viewPager.currentItem = 2
            try {
                if (newCount == null) {
                    enterCo2Button.visibility = View.VISIBLE
                    imageView2.setImageResource(R.drawable.carbon_footprint)
                    co2counter.setText(R.string.defaultValue)
                    circle.progress = 0
                    checkWeekdayAndUpdateLineChart(0f)
                    Log.d("TodayObserver", "observer1: nothing saved in todays database")
                } else {
                    if (newCount.toFloat() > 1840000.0){
                        enterCo2Button.visibility = View.GONE
                        showDialogMaxEntered()
                        imageView2.setImageResource(R.drawable.exclamation)
                    } else if (newCount.toFloat() in 46000.0..1840000.0){
                        showDialogAverage()
                    }
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
            } catch (e: Exception){
                Log.d("newCount Error", e.message)
            }
        }

        // Create the observer which updates the progressbar and linechart for weekdays
        val weeklyCo2CountObserver = Observer<List<String>> { count ->
            try {
                if (count != null) {
                for (i in 0..6){
                    listOfWeekCountsForProgressbar[i] = count[i].toFloat().toInt()
                    listOfWeekCountsForLineChart[i] = count[i].toFloat()/1000
                    setLineChart(listOfWeekCountsForLineChart)
                    if (listOfWeekCountsForLineChart[i] > 46f){
                        listOfWeekCountsForLineChart[i] = 46f
                        lineChart.invalidate()

                    }
                }
                    val yesterday = viewModel.getYesterdaysWeekday()
                    co2counterFragOne.text = (String.format("%.2f", listOfWeekCountsForLineChart[yesterday!!]) + " kg")
                    circleFragOne.progress = listOfWeekCountsForProgressbar[yesterday]

                    Log.d("weekObserver", "observer2: new count for weekday $listOfWeekCountsForLineChart")
                }

            } catch (e: Exception){
                Log.d("newCount Error", e.message)
            }
        }

        // Observe the LiveData, passing in this activity as the LifeCycleOwner and the observer.
        viewModel.accumulatedCo2Count.observe(this, co2CountObserver)
        viewModel.changesToWeeklyCounts.observe(this, weeklyCo2CountObserver)

        // Add default values to progressbar and linechart array
        if (listOfWeekCountsForProgressbar.isEmpty()){
            for (i in 0..6){
                listOfWeekCountsForProgressbar.add(i,0)
                listOfWeekCountsForLineChart.add(i, 0f)
            }
        }

        if (yValues.isEmpty()){
            // Add default values to linechart
            yValues.add(Entry(0f, 0f, "0"))
            yValues.add(Entry(1f, 0f, "1"))
            yValues.add(Entry(2f, 0f, "2"))
            yValues.add(Entry(3f, 0f, "3"))
            yValues.add(Entry(4f, 0f, "4"))
            yValues.add(Entry(5f, 0f, "5"))
            yValues.add(Entry(6f, 0f, "6"))
        }


        // Set customization and preferences
        set1 = LineDataSet(yValues, "DataSet 1")
        set1?.color = ContextCompat.getColor(this, R.color.colorButtonLightGreen)
        set1?.setCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
        set1?.lineWidth = 5f
        set1?.circleRadius = 5f
        set1?.valueTextSize = 0f
        set1?.setDrawFilled(false)
        set1?.setDrawHighlightIndicators(false)

        // Highlighting
        set1?.isHighlightEnabled = true
        set1?.highLightColor = (ContextCompat.getColor(this, R.color.colorButtonLightGreen))
        set1?.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        set2 = LineDataSet(yValueHighlight, "DataSet 2")

        set2?.setCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
        set2?.setDrawCircleHole(true)
        set2?.circleRadius = 10f
        set2?.valueTextSize = 0f
        set2?.circleHoleRadius = 5f
        set2?.circleHoleColor = ContextCompat.getColor(this, R.color.colorButtonDarkGreen)


        lineChart.setOnChartValueSelectedListener(this)

        dataSets.add(set1!!)
        dataSets.add(set2!!)
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

        // lineChart customization
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(true)
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = true
        lineChart.axisRight.axisMaximum = 49f //46f er max
        lineChart.axisLeft.axisMaximum = 49f //46f er max
        lineChart.axisRight.axisMinimum = -2f
        lineChart.axisLeft.axisMinimum = -2f
        lineChart.setExtraOffsets(10f, 10f, 10f, 10f)

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


        // Alarm manager
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
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

        // Code to set alarm. Commented out for now
//        alarmMgr?.setExact(
//            AlarmManager.RTC,
//            calendar.timeInMillis,
//            alarmIntent
//        )

    }

    // Function to update todays count in LineChart
    private fun checkWeekdayAndUpdateLineChart(value: Float){

        when (now.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(1, value.toString(), currentDate)
                weekDays[0] = "I dag"
            }
            Calendar.TUESDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(2, value.toString(), currentDate)
                weekDays[1] = "I dag"
            }
            Calendar.WEDNESDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(3, value.toString(), currentDate)
                weekDays[2] = "I dag"
            }
            Calendar.THURSDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(4, value.toString(), currentDate)
                weekDays[3] = "I dag"
            }
            Calendar.FRIDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(5, value.toString(), currentDate)
                weekDays[4] = "I dag"
            }
            Calendar.SATURDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(6, value.toString(), currentDate)
                weekDays[5] = "I dag"
            }
            Calendar.SUNDAY -> {
                inputIntoWeeklyTable = DailyCo2Count(7, value.toString(), currentDate)
                weekDays[6] = "I dag"
            }
        }
        viewModel.insertDailyCount(inputIntoWeeklyTable)
        yValueHighlight.clear()
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

        when(item.itemId){
            android.R.id.home -> {
                showDialogInfo()
            }
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

        viewModel.insertEmptyValuesIntoWeekTable()
        circle.progress = 0

        for (i in 0..6){
            listOfWeekCountsForProgressbar[i] = 0
            listOfWeekCountsForLineChart[i] = 0f
        }

        yValueHighlight.clear()
        lineChart.invalidate()

    }

    // Function to reset input for today
    private fun resetInputToday(){
        countTodayForLineChart = 0f
        countTodayForProgressbar = 0
        circle.progress = 0
        yValueHighlight.clear()
        checkWeekdayAndUpdateLineChart(0f)
    }

    // Function to show the Dialog
    private fun showDialogInfo(){
        dialogInfo = Dialog(this)
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogInfo.setContentView(R.layout.activity_dialog)
        dialogInfo.setTitle("info")

        button = dialogInfo.findViewById<View>(R.id.button_ok) as Button
        button.isEnabled = true
        button.setOnClickListener {
            dialogInfo.cancel()
        }
        dialogInfo.show()

    }

    private fun showDialogMaxEntered(){
        dialogMaxEntered = Dialog(this)
        dialogMaxEntered.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogMaxEntered.setContentView(R.layout.activity_dialog_max_entered)
        dialogMaxEntered.setTitle("max_entered")

        button = dialogMaxEntered.findViewById<View>(R.id.button_ok) as Button
        button.setOnClickListener{
            dialogMaxEntered.cancel()
        }
        dialogMaxEntered.show()
    }

    private fun showDialogAverage(){
        dialogAverageReached = Dialog(this)
        dialogAverageReached.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAverageReached.setContentView(R.layout.activity_dialog_average)
        dialogAverageReached.setTitle("average")

        button = dialogAverageReached.findViewById<View>(R.id.button_ok) as Button
        button.setOnClickListener{
            dialogAverageReached.cancel()
        }
        dialogAverageReached.show()
    }

    override fun onNothingSelected() {
        yValueHighlight.clear()
    }

    // Function to update LineChart with correct values when weekday in LineChart is selected
    override fun onValueSelected(e: Entry?, h: Highlight?) {

        viewPager.currentItem = 2

        if (e != null) {
            val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
            yValueHighlight.clear()

            when(e.data) {
                "0" -> {
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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
                    yValueHighlight.add(e)
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

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

        private val fragmentList : MutableList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]

        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment : Fragment) {
            fragmentList.add(fragment)

        }

    }

}
