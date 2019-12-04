package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHeatingBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.HeatingViewModel
import kotlinx.android.synthetic.main.activity_el.*

class HeatingActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            HeatingViewModel::class.java
        )

        // Bind this activity to the layout xml file using databinding
        val binding: ActivityHeatingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_heating)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        // ButtonClick
        var heatingType = ""
        saveCo2Btn.setOnClickListener {
            viewModel.onSaveCo2ButtonClick(heatingType)
        }

        // Function to change live calculated output based on input
        fun textChanged(co2Number: Double) {
            enterKWt.text = null
            calculatedCo2TextField.text = null
            enterKWt.addTextChangedListener(object : TextWatcher {
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
                    if (enterKWt.text.isNotEmpty()) {
                        val kWt = enterKWt.text.toString().toDouble()
                        val calculatedHeatingCo2 = kWt * co2Number
                        calculatedCo2TextField.text = String.format("%.2f", calculatedHeatingCo2)
                    } else {
                        calculatedCo2TextField.text = null

                    }
                }
            })
        }

        // Spinner configuration
        val sp: Spinner = findViewById(R.id.spinner)
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                toast("Vælg venligst en varmekilde i menuen.")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        textChanged(0.080)
                        heatingType = "Fjernvarme"
                    }
                    1 -> {
                        textChanged(0.142)
                        heatingType = "Bygas"
                    }
                    2 -> {
                        textChanged(0.041)
                        heatingType = "Fjernkøling"
                    }
                }

            }
        }
    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, EnterCo2Activity::class.java))
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    override fun onSuccess() {
        enterKWt.hideKeyboard()

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