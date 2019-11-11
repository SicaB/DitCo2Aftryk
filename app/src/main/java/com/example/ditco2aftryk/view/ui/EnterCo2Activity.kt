package com.example.ditco2aftryk.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.example.ditco2aftryk.R
import kotlinx.android.synthetic.main.activity_enter_co2.*

class EnterCo2Activity : AppCompatActivity(), Actionbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_co2)

//        // Actionbar
        home.setNavigationIcon(R.drawable.ic_home_black_24dp)
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

    }


    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    override fun onHomeButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
    }

    fun onFlightButtonClicked(v: View) {
        startActivity(Intent(this, FlightCo2Activity::class.java))
    }

    fun onClothesButtonClicked(view: View) {}
    fun onCarButtonClicked(view: View) {
        startActivity(Intent(this, CarActivity::class.java))
    }

    fun onMeatButtonClicked(view: View) {
        startActivity(Intent(this, MeatActivity::class.java))

    }
    fun onTrainButtonClicked(view: View) {
        startActivity(Intent(this, TrainActivity::class.java))

    }
    fun onHeatingButtonClicked(view: View) {}
    fun onBusButtonClicked(view: View) {
        startActivity(Intent(this, BusActivity::class.java))
    }
    fun onElButtonClicked(view: View) {
        startActivity(Intent(this, ElActivity::class.java))
    }
}
