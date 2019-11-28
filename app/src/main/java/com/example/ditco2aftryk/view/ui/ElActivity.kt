package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityElBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.ElViewModel
import kotlinx.android.synthetic.main.activity_el.*

class ElActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            ElViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityElBinding = DataBindingUtil.setContentView(this, R.layout.activity_el)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        enterKWt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (enterKWt.text.isNotEmpty()) {
                    val kWt = enterKWt.text.toString().toDouble()
                    val calculatedElCo2 = kWt * 0.244
                    calculatedCo2TextField.text = String.format("%.2f", calculatedElCo2)
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