package com.example.ditco2aftryk.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityFlightCo2Binding
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModel
import kotlinx.android.synthetic.main.activity_flight_co2.*

class ClothesActivity : AppCompatActivity(), Listener, Actionbar {

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

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackButtonClicked(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHomeButtonClicked(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
