package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ditco2aftryk.R
import kotlinx.android.synthetic.main.activity_enter_co2.*
import kotlinx.android.synthetic.main.activity_home_screen.*

class EnterCo2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_co2)

        transportBtn.setOnClickListener {
            // intent is used to start a new activity
            val intent = Intent(this, TransportActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
    }
}
