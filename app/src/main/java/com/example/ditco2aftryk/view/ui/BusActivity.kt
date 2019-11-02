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
import com.example.ditco2aftryk.databinding.ActivityBusBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.BusViewModel
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.activity_flight_co2.back
import kotlinx.android.synthetic.main.activity_flight_co2.calculatedCo2TextField
import kotlinx.android.synthetic.main.activity_flight_co2.home

class BusActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            BusViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityBusBinding = DataBindingUtil.setContentView(this, R.layout.activity_bus)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        enterKmDriven.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (enterKmDriven.text.isNotEmpty()) {
                    val kmDriven = enterKmDriven.text.toString().toDouble()
                    val calculatedBusCo2 = kmDriven * 0.069
                    calculatedCo2TextField.text = String.format("%.2f", calculatedBusCo2)
                }
            }
        })

    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, EnterCo2Activity::class.java))
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    override fun onSuccess() {
        enterKmDriven.hideKeyboard()

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


