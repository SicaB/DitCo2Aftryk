package com.example.ditco2aftryk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.ditco2aftryk.model.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val serviceIntent: Intent? = Intent(context, AlarmService::class.java)
            context?.startService(serviceIntent)

        Toast.makeText(
            context, "Daily count has been saved!",
            Toast.LENGTH_LONG
        ).show()
    }
}