package com.example.ditco2aftryk.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.example.ditco2aftryk.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener


/*  I viewModellen skrives bagvedliggende data kode som skal sepereres fra UI.
    Dvs kode som fx kan ændre sig og som skal vises i UI.
    selve koden som viser UI skal skrives i aktiviteten/fragmentet. */

class HomeScreenViewModel(private val repository: Co2CountRepository) : ViewModel() {

    // LiveData is implemented to notify the activity for changes
    val co2Count: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //val co2CountObserver = Observer<String>

//    init {
//        repository.co2Count.observe(this, co2CountObserver)
//    }


    var listener: Listener? = null

    fun getCo2Count() = repository.getCo2Count()







}



