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
        back.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

    }

    override fun onBackButtonClicked(v: View?){
        startActivity(Intent(this, HomeScreenActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onHomeButtonClicked(v: View?){
    }

    fun onFlightButtonClicked(view: View) {
        startActivity(Intent(this, FlightActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    fun onClothesButtonClicked(view: View) {
        startActivity(Intent(this, ClothesActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    fun onCarButtonClicked(view: View) {
        startActivity(Intent(this, CarActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    fun onMeatButtonClicked(view: View) {
        startActivity(Intent(this, MeatActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()

    }
    fun onTrainButtonClicked(view: View) {
        startActivity(Intent(this, TrainActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()

    }
    fun onHeatingButtonClicked(view: View) {
        startActivity(Intent(this, HeatingActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    fun onBusButtonClicked(view: View) {
        startActivity(Intent(this, BusActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    fun onElButtonClicked(view: View) {
        startActivity(Intent(this, ElActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
}
