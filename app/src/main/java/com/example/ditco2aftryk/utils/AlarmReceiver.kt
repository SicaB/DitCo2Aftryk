package com.example.ditco2aftryk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.ditco2aftryk.model.AlarmService
import com.example.ditco2aftryk.model.repositories.Co2CountRepository

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val serviceIntent: Intent? = Intent(context, AlarmService::class.java)
            context?.startService(serviceIntent)

        //Log.d("alarmmanager", "Daily Co2 has been saved")

        Toast.makeText(
            context, "Alarm went off",
            Toast.LENGTH_LONG
        ).show()
    }
}