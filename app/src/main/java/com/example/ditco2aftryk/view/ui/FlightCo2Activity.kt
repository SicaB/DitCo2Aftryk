package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityFlightCo2Binding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModel
import kotlinx.android.synthetic.main.activity_flight_co2.*

class FlightCo2Activity : AppCompatActivity(), Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityFlightCo2Binding = DataBindingUtil.setContentView(this, R.layout.activity_flight_co2)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            FlightCo2ViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this
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
