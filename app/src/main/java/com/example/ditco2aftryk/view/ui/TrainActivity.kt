package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityTrainBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.TrainViewModel
import kotlinx.android.synthetic.main.activity_bus.enterKmDriven
import kotlinx.android.synthetic.main.activity_flight.back
import kotlinx.android.synthetic.main.activity_flight.calculatedCo2TextField
import kotlinx.android.synthetic.main.activity_flight.home

class TrainActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProvider(this).get(
            TrainViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityTrainBinding = DataBindingUtil.setContentView(this, R.layout.activity_train)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        enterKmDriven.text = null
        calculatedCo2TextField.text = null
        enterKmDriven.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (enterKmDriven.text.isNotEmpty() && enterKmDriven.text.toString() != ".") {
                    val kmDriven = enterKmDriven.text.toString().toDouble()
                    val calculatedBusCo2 = kmDriven * 0.065
                    calculatedCo2TextField.text = String.format("%.2f", calculatedBusCo2)
                } else {
                    calculatedCo2TextField.text = null
                }
            }
        })
    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, EnterCo2Activity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()

    }

    override fun onSuccess() {
        enterKmDriven.hideKeyboard()

        toast("Dit co2 aftryk er gemt")
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onFailure(message: String) {
        toast(message)
    }
}
