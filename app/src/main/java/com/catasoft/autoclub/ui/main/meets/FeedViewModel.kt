package com.catasoft.autoclub.ui.main.meets;

import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.IMeetsRepository
import com.catasoft.autoclub.util.getAvatarDownloadUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel

@Inject
constructor(
    private val mMeetsRepository: IMeetsRepository
): ViewModel(){

    val userAvatarUriLive: MutableLiveData<Uri> = MutableLiveData()
    val meetsList: MutableLiveData<List<Meet>> = MutableLiveData()

    fun getMeets(){
        viewModelScope.launch {
            val meets = mMeetsRepository.getAllMeets()
            meetsList.postValue(meets)
//            Timber.e(meets.toString())
        }
    }

    fun getClosetsMeets(userLocation: Location){
        viewModelScope.launch {
            var meets = mMeetsRepository.getAllMeets()
            meets = meets?.let {
                return@let it.sortedBy { meet ->
                    val meetLoc = Location(LocationManager.NETWORK_PROVIDER)
                    meetLoc.latitude = meet.placeLat!!
                    meetLoc.longitude = meet.placeLong!!
                    return@sortedBy userLocation.distanceTo(meetLoc)
                }
            }
            meetsList.postValue(meets)
            Timber.e(meets.toString())
        }
    }

    init {
        viewModelScope.launch {
            userAvatarUriLive.postValue(CurrentUser.getEntity().getAvatarDownloadUri())
        }
    }
}
