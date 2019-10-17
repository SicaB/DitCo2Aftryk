package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/*  I viewModellen skrives bagvedliggende data kode som skal sepereres fra UI.
    Dvs kode som fx kan ændre sig og som skal vises i UI.
    selve koden som viser UI skal skrives i aktiviteten/fragmentet. */

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    // the ViewModel maintains a reference to the repository to get data
    // then LiveData gives us updated data when they change

    init {
        // inside here we get reference to dao from roomDatabase to construct the correct repository
    }

    // LiveData implemented to notify the activity for changes
    val changeNotifier = MutableLiveData<Int>()
    // The activity observes changes to changeNotifier. When the value changes, the activity updates with that value
    // This function adds one to the value count from the constructor

}



