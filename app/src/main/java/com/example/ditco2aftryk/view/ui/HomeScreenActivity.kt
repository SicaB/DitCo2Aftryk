package com.example.ditco2aftryk.view.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
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
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenActivity : AppCompatActivity(), Listener, OnChartValueSelectedListener {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityHomeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        viewModel.listener = this

        // Create the observer which updates the UI
        val co2CountObserver = Observer<String> { newCount ->
            co2counter.text = "0.0 Kg"
            // Update UI with current data
            if (newCount != null) {
                co2counter.text = (String.format("%.2f", newCount.toDouble()/1000) + " kg")
            }
            val circleProcess: Double? = newCount?.toDouble()
            if (circleProcess != null) {
                val newNumber = (circleProcess.toInt())
                circle?.progress = newNumber
                Log.d("mytag", "number $newNumber")
            }
        }

        // Create the observer which updates the UI
        val dailyCo2CountObserver = Observer<String> {newCount ->
            dailyCo2counter.text = "0.0 kg"
            if(newCount!= null){
                dailyCo2counter.text = (String.format("%.2f", newCount.toDouble()/1000) + " Kg")
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

        // Initialiserer linechart
        val lineChart: LineChart? = findViewById(R.id.line_chart)

        val yValues = ArrayList<Entry>()
        yValues.add(Entry(0f, 30f, "0"))
        yValues.add(Entry(1f, 8f, "1"))
        yValues.add(Entry(2f, 15f, "2"))
        yValues.add(Entry(3f, 8f, "3"))
        yValues.add(Entry(4f, 25f, "4"))
        yValues.add(Entry(5f, 10f, "5"))
        yValues.add(Entry(6f, 22f, "6"))

        val set1: LineDataSet
        set1 = LineDataSet(yValues, "DataSet 1")

        // Set preferences
        set1.color = ContextCompat.getColor(this, R.color.colorButtonLightGreen)
        set1.setCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
        set1.lineWidth = 5f
        set1.circleRadius = 5f
        set1.valueTextSize = 0f
        set1.setDrawCircleHole(true)
        set1.setDrawFilled(false)
        set1.setDrawHighlightIndicators(false)
        set1.isHighlightEnabled = true
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)

        // set data
        lineChart?.data = data
        lineChart?.description?.isEnabled = false
        lineChart?.legend?.isEnabled = false
        lineChart?.setPinchZoom(false)

        lineChart?.setOnChartValueSelectedListener(this)

        // position of x-axis and counts of labels
        lineChart?.xAxis?.labelCount = 5
        lineChart?.xAxis?.position = XAxis.XAxisPosition.TOP

        lineChart?.axisLeft?.setDrawGridLines(false)
        lineChart?.axisRight?.setDrawGridLines(false)
        lineChart?.xAxis?.setDrawGridLines(true)
        lineChart?.axisRight?.isEnabled = false
        lineChart?.axisLeft?.isEnabled = false

        // Text customization
        lineChart?.xAxis?.textColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart?.xAxis?.textSize = 12f

        // array to hold week days
        val weekDays = ArrayList<String>()
        weekDays.add("man.")
        weekDays.add("tirs.")
        weekDays.add("ons.")
        weekDays.add("tors.")
        weekDays.add("fre.")
        weekDays.add("lør.")
        weekDays.add("søn.")

        // Set the weekdays to the x-axis
        lineChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(weekDays)

        // Grid customization
//        lineChart?.axisRight?.enableGridDashedLine(5f, 5f, 0f)
//        lineChart?.axisLeft?.enableGridDashedLine(5f, 5f, 0f)
//        lineChart?.xAxis?.enableGridDashedLine(5f, 5f, 0f)
        lineChart?.xAxis?.enableAxisLineDashedLine(8f, 18f, 0f)
        lineChart?.xAxis?.gridColor = ContextCompat.getColor(this, R.color.colorBlack)
        lineChart?.xAxis?.gridLineWidth = 2f
        lineChart?.xAxis?.axisLineColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart?.xAxis?.axisLineWidth = 2f

        // Alarm manager.
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            //intent.putExtra("accumulatedCountForTheDay", viewModel.accumulatedCo2Count)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 52)
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

    //ensure that we don't set reminder for the past
    private fun checkTime(reminder: Calendar) {
        val now = Calendar.getInstance()
        if (reminder.before(now)) {
            val alarmForFollowingDay = reminder.timeInMillis + 86400000L
            reminder.timeInMillis = alarmForFollowingDay
        }
    }

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess() {

    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
