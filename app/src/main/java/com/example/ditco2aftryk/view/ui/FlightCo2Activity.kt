package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityFlightCo2Binding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModel
import kotlinx.android.synthetic.main.activity_flight_co2.*

class FlightCo2Activity : AppCompatActivity(), Listener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            FlightCo2ViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityFlightCo2Binding = DataBindingUtil.setContentView(this, R.layout.activity_flight_co2)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        enterHoursFlown.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!enterHoursFlown.text.isEmpty()) {
                    val hoursFlown = enterHoursFlown.text.toString().toDouble()
                    val calculatedFlightCo2 = hoursFlown * 0.092
                    calculatedCo2TextField.text = String.format("%.2f", calculatedFlightCo2)
                    Log.d("TAG", "hej hej")
                }
            }
        })
    }

    override fun onSuccess() {
        enterHoursFlown.hideKeyboard()

        toast("co2 count saved")
        // intent is used to start a new activity
        val intent = Intent(this, HomeScreenActivity::class.java)
        // start activity
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        toast(message)
    }
}
