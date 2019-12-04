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
import com.example.ditco2aftryk.databinding.ActivityClothesBinding
import com.example.ditco2aftryk.utils.hideKeyboard
import com.example.ditco2aftryk.utils.toast
import com.example.ditco2aftryk.view.viewmodel.ClothesViewModel
import kotlinx.android.synthetic.main.activity_clothes.*


class ClothesActivity : AppCompatActivity(), Listener, Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            ClothesViewModel::class.java)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityClothesBinding = DataBindingUtil.setContentView(this, R.layout.activity_clothes)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // define listener to the viewModel
        viewModel.listener = this

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        enterKgPurchased.text = null
        calculatedCo2TextField.text = null
        enterKgPurchased.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (enterKgPurchased.text.isNotEmpty()) {
                    val kgPurchased = enterKgPurchased.text.toString().toDouble()
                    val calculatedClothesCo2 = kgPurchased * 20
                    calculatedCo2TextField.text = String.format("%.2f", calculatedClothesCo2)
                }else {
                    calculatedCo2TextField.text = null
                }
            }
        })
    }

    override fun onSuccess() {
        enterKgPurchased.hideKeyboard()

        toast("Dit co2 aftryk er gemt")
        // intent is used to start a new activity
        val intent = Intent(this, HomeScreenActivity::class.java)
        // start activity
        startActivity(intent)
        finish()
    }

    override fun onFailure(message: String) {
        toast(message)    }

    override fun onBackButtonClicked(v: View?) {
        startActivity(Intent(this, EnterCo2Activity::class.java))
        finish()
    }

    override fun onHomeButtonClicked(v: View?) {
        startActivity(Intent(this, HomeScreenActivity::class.java))
        finish()
    }
}
