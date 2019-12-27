package com.mark.sharee.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.prefs.Preferences

//@Singleton
class BaseViewModelFactory constructor(val application: Application,
                                               var preferences: Preferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        // EXAMPLE :
//        if (modelClass.isAssignableFrom(BusinessHistoryViewModel::class.java)) {
//            return BusinessHistoryViewModel(repository, application) as T
//        }

//        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
//            return MovieViewModel()
//        }

        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}