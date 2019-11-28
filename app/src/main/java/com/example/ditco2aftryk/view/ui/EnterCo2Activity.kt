package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ditco2aftryk.R
import kotlinx.android.synthetic.main.activity_enter_co2.*

class EnterCo2Activity : AppCompatActivity(), Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_co2)

        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)

    }

    override fun onBackButtonClicked(v: View?){
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    fun onFlightButtonClicked(view: View) {
        startActivity(Intent(this, FlightActivity::class.java))
    }

    fun onClothesButtonClicked(view: View) {
        startActivity(Intent(this, ClothesActivity::class.java))
    }
    fun onCarButtonClicked(view: View) {
        startActivity(Intent(this, CarActivity::class.java))
    }

    fun onMeatButtonClicked(view: View) {
        startActivity(Intent(this, MeatActivity::class.java))

    }
    fun onTrainButtonClicked(view: View) {
        startActivity(Intent(this, TrainActivity::class.java))

    }
    fun onHeatingButtonClicked(view: View) {
        startActivity(Intent(this, HeatingActivity::class.java))
    }

    fun onBusButtonClicked(view: View) {
        startActivity(Intent(this, BusActivity::class.java))
    }
    fun onElButtonClicked(view: View) {
        startActivity(Intent(this, ElActivity::class.java))
    }
}
