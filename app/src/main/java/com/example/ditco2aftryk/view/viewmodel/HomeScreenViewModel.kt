package com.example.ditco2aftryk.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ditco2aftryk.model.Co2Count
import com.example.ditco2aftryk.repositories.Co2CountRepository


/*  I viewModellen skrives bagvedliggende data kode som skal sepereres fra UI.
    Dvs kode som fx kan ændre sig og som skal vises i UI.
    selve koden som viser UI skal skrives i aktiviteten/fragmentet. */

class HomeScreenViewModel : ViewModel() {

    var co2Count: Int? = null

    // the ViewModel maintains a reference to the repository to get data
    // private val repository: Co2CountRepository

    // then LiveData is implemented to notify the activity for changes
    //val co2Count: LiveData<List<Co2Count>>





}



