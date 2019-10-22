package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModelFactory
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create instances (do this with dependency injection later)
        val database = AppDatabase(this)
        val repository = Co2CountRepository(database)
        val factory = HomeScreenViewModelFactory(repository)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityHomeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this, factory).get(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        viewModel.getCo2Count().observe(this, Observer{ co2Count ->
            // TODO update count

        })

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {
            // intent is used to start a new activity
            val intent = Intent(this, EnterCo2Activity::class.java)
            // start next activity
            startActivity(intent)


        }
    }
}
