package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.ActivityHomeScreenBinding
import com.example.ditco2aftryk.view.viewmodel.HomeScreenViewModel
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreenActivity : AppCompatActivity(), Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind this activity to the layout xml file using databinding
        val binding : ActivityHomeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        // create the viewModel
        val viewModel = ViewModelProviders.of(this).get(
            HomeScreenViewModel::class.java)

        // bind this activity to the viewModel
        binding.viewmodel = viewModel

        viewModel.listener = this

        // Create the observer which updates the UI
        val co2CountObserver = Observer<String> { newCount ->
            // Update UI with current data
            co2counter.text = newCount
        }

        // Observe the LiveData, passing in this activity as the LifeCycleOwner and the observer.
        viewModel.accumulatedCo2Count.observe(this, co2CountObserver)

        // start new activity when clicking on Enter co2 button
        enterCo2Button.setOnClickListener {

            // intent is used to start a new activity
            val intent = Intent(this, EnterCo2Activity::class.java)
            // start next activity
            startActivity(intent)
        }
    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
