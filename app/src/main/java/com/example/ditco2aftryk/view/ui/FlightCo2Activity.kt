package com.example.ditco2aftryk.view.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityFlightCo2Binding
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.util.hideKeyboard
import com.example.ditco2aftryk.view.ui.util.toast
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModel
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModelFactory
import kotlinx.android.synthetic.main.activity_flight_co2.*

class FlightCo2Activity : AppCompatActivity(), Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create instances (do this with dependency injection later)
        val database = AppDatabase(this)
        val repository = Co2CountRepository(database)
        val factory = FlightCo2ViewModelFactory(repository)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityFlightCo2Binding = DataBindingUtil.setContentView(this, R.layout.activity_flight_co2)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this, factory).get(
            FlightCo2ViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this


//        viewModel.getCo2Count().observe(this, Observer{ co2Count ->
//            // TODO update count
//
//        })

    }

    override fun onSucces() {
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
