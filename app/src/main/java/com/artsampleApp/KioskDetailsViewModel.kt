package com.artsampleApp

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artsampleApp.models.Kiosk

class KioskDetailsViewModel : ViewModel() {

    lateinit var kiosk : Kiosk

    val name = MutableLiveData<String>().apply {
        value = ""
    }

    val address = MutableLiveData<String>().apply {
        value = ""
    }

    val id = MutableLiveData<String>().apply {
        value = ""
    }

    val identifier = MutableLiveData<String>().apply {
        value = ""
    }

    val lastReportedDate = MutableLiveData<String>().apply {
        value = ""
    }

    val updatedDate = MutableLiveData<String>().apply {
        value = ""
    }

    val serial = MutableLiveData<String>().apply {
        value = ""
    }

    val online = MutableLiveData<String>().apply {
        value = ""
    }

    val active = MutableLiveData<String>().apply {
        value = ""
    }



    fun update()
    {
        name.value = kiosk.name
        address.value = kiosk.address
        id.value = kiosk.id.toString()
        identifier.value = kiosk.identifier
        lastReportedDate.value = kiosk.lastReported
        updatedDate.value = kiosk.updated
        serial.value = kiosk.serial
        online.value = kiosk.isOnline.toString()
        active.value = kiosk.isActive.toString()
    }
}