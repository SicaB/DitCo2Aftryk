package com.example.ditco2aftryk.view.viewmodel

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.ditco2aftryk.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener

class FlightCo2ViewModel(private val repository: Co2CountRepository) : ViewModel(){

    var flightCo2Input: String? = null

    var listener: Listener? = null

    fun onSaveCo2ButtonClick(view: View){
        if(!flightCo2Input?.isDigitsOnly()!! || flightCo2Input.isNullOrEmpty()){
            listener?.onFailure("Invalid co2 value entered.")
            return
        }

        listener?.onSucces()

    }

}