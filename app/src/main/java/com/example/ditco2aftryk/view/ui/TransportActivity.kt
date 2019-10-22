package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ditco2aftryk.R
import kotlinx.android.synthetic.main.activity_transport.*

class TransportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport)

        saveCo2Btn.setOnClickListener {
            // intent is used to start a new activity
            val intent = Intent(this, FlightCo2Activity::class.java)
            // start your next activity
            startActivity(intent)


        }
    }
}
