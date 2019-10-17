package com.example.ditco2aftryk.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.view.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // I define the mainActivityViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel

    // if i wanted a recyclerview - this is were i would define it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // associate this activity to the viewModel
       mainActivityViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainActivityViewModel::class.java)


        enterCo2Button.setOnClickListener {
            // intent is used to start a new activity
            val intent = Intent(this, EnterCo2Activity::class.java)
            // start your next activity
            startActivity(intent)


        }
    }
}
