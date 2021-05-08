package com.catasoft.autoclub.ui.main.addmeet

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class AddMeetViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val liveLocationId: MutableLiveData<String> = MutableLiveData()
    val liveLocationName: MutableLiveData<String> = MutableLiveData()
    val liveLocationPhoto: MutableLiveData<Uri> = MutableLiveData()
    val liveLocationLat: MutableLiveData<Double> = MutableLiveData()
    val liveLocationLong: MutableLiveData<Double> = MutableLiveData()

    companion object {
        val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}
