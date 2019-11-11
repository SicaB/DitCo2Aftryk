package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityMeatBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.MeatViewModel
import kotlinx.android.synthetic.main.activity_flight_co2.back
import kotlinx.android.synthetic.main.activity_flight_co2.calculatedCo2TextField
import kotlinx.android.synthetic.main.activity_flight_co2.home
import kotlinx.android.synthetic.main.activity_meat.*

class MeatActivity : AppCompatActivity(), Listener, Actionbar  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            MeatViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityMeatBinding = DataBindingUtil.setContentView(this, R.layout.activity_meat)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        enterKgCooked.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (enterKgCooked.text.isNotEmpty()) {
                    val kgCooked = enterKgCooked.text.toString().toDouble()
                    val calculatedMeatCo2 = kgCooked * 19.4
                    calculatedCo2TextField.text = String.format("%.2f", calculatedMeatCo2)
                }
            }
        })

        val sp : Spinner = findViewById(R.id.spinner) as Spinner

        val carOptions = arrayOf("Oksekød", "Svinekød", "Kyllingekød", "Lammekød")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, carOptions)
        sp.adapter = adapter

        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                toast("Please select meat type.")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

    }

    override fun onBackButtonClicked(v: View?) {
        startActivity(Intent(this, EnterCo2Activity::class.java))
    }

    override fun onHomeButtonClicked(v: View?) {
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    override fun onSuccess() {
        enterKgCooked.hideKeyboard()

        toast("Dit co2 aftryk er gemt")
        // intent is used to start a new activity
        val intent = Intent(this, HomeScreenActivity::class.java)
        // start activity
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        toast(message)    }
}
