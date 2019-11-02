package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_home_screen.*
import java.security.KeyStore

class HomeScreenActivity : AppCompatActivity(), Listener {

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
            // Update UI with current data
            co2counter.text = newCount
        }

        // Observe the LiveData, passing in this activity as the LifeCycleOwner and the observer.
        viewModel.accumulatedCo2Count.observe(this, co2CountObserver)

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // start next activity
            startActivity(Intent(this, EnterCo2Activity::class.java))
        }

        // Initialiserer linechart
        val lineChart: LineChart = findViewById(R.id.line_chart)

        val yValues = ArrayList<Entry>()
        yValues.add(Entry(0f, 30f, "0"))
        yValues.add(Entry(1f, 8f, "1"))
        yValues.add(Entry(2f, 15f, "2"))
        yValues.add(Entry(3f, 8f, "3"))
        yValues.add(Entry(4f, 25f, "4"))
        yValues.add(Entry(5f, 10f, "5"))
        yValues.add(Entry(6f, 22f, "søn."))

        val set1: LineDataSet
        set1 = LineDataSet(yValues, "DataSet 1")

        // Set preferences
        set1.color = ContextCompat.getColor(this, R.color.colorBlack)
        set1.setCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
        set1.lineWidth = 2f
        set1.circleRadius = 4f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 0f
        set1.setDrawFilled(false)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)

        // set data
        lineChart.data = data
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setPinchZoom(true)

        // position of x-axis and counts of labels
        lineChart.xAxis.labelCount = 5
        lineChart.xAxis.position = XAxis.XAxisPosition.TOP

        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(true)
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false

        // Text customization
        lineChart.xAxis.textColor = ContextCompat.getColor(this, R.color.colorWhite)
        lineChart.xAxis.textSize = 11f

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
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(weekDays)

        // Grid customization
        lineChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        lineChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        lineChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
        lineChart.xAxis.enableAxisLineDashedLine(5f, 5f, 0f)
        lineChart.xAxis.gridColor = ContextCompat.getColor(this, R.color.colorBlack)
        lineChart.xAxis.gridLineWidth = 1.5f
        lineChart.xAxis.axisLineColor = ContextCompat.getColor(this, R.color.colorBlack)
        lineChart.xAxis.axisLineWidth = 1f


    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
