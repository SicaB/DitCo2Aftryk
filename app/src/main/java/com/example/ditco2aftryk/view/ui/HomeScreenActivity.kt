package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreenActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityHomeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        // create the viewModel
        val viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // intent is used to start a new activity
            val intent = Intent(this, EnterCo2Activity::class.java)
            // start next activity
            startActivity(intent)


        }
    }
}
