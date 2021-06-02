package com.catasoft.autoclub.ui.main.home

import android.net.Uri
import androidx.lifecycle.*
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.util.getAvatarDownloadUri
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.util.getUserAvatarUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel

@Inject
constructor(
) : ViewModel() {

    val avatarUri: MutableLiveData<Uri?> = MutableLiveData()

    fun getAvatarDownloadUri(userUid: String)
    {
        viewModelScope.launch {
            kotlin.runCatching {
                getUserAvatarUri(userUid)
            }.onSuccess {
                avatarUri.postValue(it)
            }
        }
    }

}