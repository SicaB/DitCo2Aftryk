package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.MyCo2Count
import com.example.ditco2aftryk.model.MyCo2CountDatabase
import com.example.ditco2aftryk.repositories.Co2CountRepository
import kotlinx.coroutines.launch

/*  I viewModellen skrives bagvedliggende data kode som skal sepereres fra UI.
    Dvs kode som fx kan ændre sig og som skal vises i UI.
    selve koden som viser UI skal skrives i aktiviteten/fragmentet. */

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    // the ViewModel maintains a reference to the repository to get data
    private val repository: Co2CountRepository
    // then LiveData is implemented to notify the activity for changes
    val allCo2Counts: LiveData<List<MyCo2Count>>

    init {
        // inside here we get reference to dao from roomDatabase to construct the correct repository
        val myCo2CountDao = MyCo2CountDatabase.getDatabase(application).myCo2CountDao()
        repository = Co2CountRepository(myCo2CountDao)
        allCo2Counts = repository.allCo2Counts
    }

   fun insert(arg: MyCo2Count) = viewModelScope.launch {
       repository.insert(arg)
   }

   fun update(arg: MyCo2Count) = viewModelScope.launch {
       repository.update(arg)
   }

   fun delete(arg: MyCo2Count) = viewModelScope.launch {
       repository.delete(arg)
   }


}



