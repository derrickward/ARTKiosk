package com.artsampleApp

import android.content.Context
import android.location.Location
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artsampleApp.repository.KioskRepository

class KioskListViewModel : ViewModel() {

    val progressVisibility = MutableLiveData<Int>().apply {
        value = View.GONE
    }

    val isEnabled = MutableLiveData<Boolean>().apply {
        value = true
    }

    val loadingText = MutableLiveData<String>().apply {
        value = ""
    }

    fun load(context: Context, lat:Double,long:Double,completion: (isSuccessful : Boolean) -> Unit)
    {
        KioskRepository.loadKiosks(context,lat,long,50)
        { kiosks, isSuccessful ->

            completion(isSuccessful)
        }
    }
}