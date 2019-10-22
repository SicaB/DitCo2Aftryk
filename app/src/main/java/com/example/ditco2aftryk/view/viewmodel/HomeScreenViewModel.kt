package com.example.ditco2aftryk.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ditco2aftryk.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener


/*  I viewModellen skrives bagvedliggende data kode som skal sepereres fra UI.
    Dvs kode som fx kan ændre sig og som skal vises i UI.
    selve koden som viser UI skal skrives i aktiviteten/fragmentet. */

class HomeScreenViewModel(private val repository: Co2CountRepository) : ViewModel() {

    var listener: Listener? = null

    fun getCo2Count() = repository.getCo2Count()


    // the ViewModel maintains a reference to the repository to get data
    // private val repository: Co2CountRepository

    // then LiveData is implemented to notify the activity for changes
    //val co2Count: LiveData<List<Co2Count>>





}



