package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ditco2aftryk.R
import kotlinx.android.synthetic.main.activity_transport.*

class TransportActivity : AppCompatActivity(), Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport)

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    }

    fun onFlightButtonClicked(v: View?){
        startActivity(Intent(this, FlightCo2Activity::class.java))
    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, EnterCo2Activity::class.java))
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }
}
