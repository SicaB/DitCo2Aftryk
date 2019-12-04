package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityCarBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.CarViewModel
import kotlinx.android.synthetic.main.activity_car.*
import kotlinx.android.synthetic.main.activity_flight.back
import kotlinx.android.synthetic.main.activity_flight.calculatedCo2TextField
import kotlinx.android.synthetic.main.activity_flight.home

class CarActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            CarViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityCarBinding = DataBindingUtil.setContentView(this, R.layout.activity_car)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        // ButtonClick
        var carType = ""
        saveCo2Btn.setOnClickListener {
            viewModel.onSaveCo2ButtonClick(carType)
        }

        fun textChanged(co2Number: Double) {
            enterKmDriven.text = null
            calculatedCo2TextField.text = null
            enterKmDriven.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (enterKmDriven.text.isNotEmpty()) {
                        val kmDriven = enterKmDriven.text.toString().toDouble()
                        val calculatedCarCo2 = kmDriven * co2Number
                        calculatedCo2TextField.text = String.format("%.2f", calculatedCarCo2)
                    } else {
                        calculatedCo2TextField.text = null
                    }
                }
            })
        }

        val sp : Spinner = findViewById(R.id.spinner)

            sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    toast("Please select a car.")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            textChanged(0.11)
                            carType = "Lille bil"
                        }
                        1 -> {
                            textChanged(0.133)
                            carType = "Mellemstor bil"
                        }
                        2 -> {
                            textChanged(0.183)
                            carType = "Stor bil"
                        }
                        3 -> {
                            textChanged(0.16)
                            carType = "Diesel bil"
                        }
                        4 -> {
                            textChanged(0.084)
                            carType = "Hybrid bil"
                        }
                        5 -> {
                            textChanged(0.043)
                            carType = "El-bil"
                        }
                    }
                }

            }


    }

    override fun onBackButtonClicked(v: View?) {
        startActivity(Intent(this, EnterCo2Activity::class.java))
        finish()
    }

    override fun onHomeButtonClicked(v: View?) {
        startActivity(Intent(this, HomeScreenActivity::class.java))
        finish()
    }

    override fun onSuccess() {
        enterKmDriven.hideKeyboard()

        toast("Dit co2 aftryk er gemt")
        // intent is used to start a new activity
        val intent = Intent(this, HomeScreenActivity::class.java)
        // start activity
        startActivity(intent)
        finish()
    }

    override fun onFailure(message: String) {
        toast(message)    }
}
