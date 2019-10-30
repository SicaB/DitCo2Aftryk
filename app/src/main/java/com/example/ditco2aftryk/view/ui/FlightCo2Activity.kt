package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityFlightCo2Binding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.FlightCo2ViewModel
import kotlinx.android.synthetic.main.activity_flight_co2.*
import kotlinx.android.synthetic.main.activity_flight_co2.back
import kotlinx.android.synthetic.main.activity_flight_co2.home

@Suppress("DEPRECATION")
class FlightCo2Activity : AppCompatActivity(), Listener, Actionbar{

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
                }
            }
        })
    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, TransportActivity::class.java))
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    override fun onSuccess() {
        enterHoursFlown.hideKeyboard()

        toast("Dit co2 aftryk er gemt")
        // intent is used to start a new activity
        val intent = Intent(this, HomeScreenActivity::class.java)
        // start activity
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        toast(message)
    }
}
