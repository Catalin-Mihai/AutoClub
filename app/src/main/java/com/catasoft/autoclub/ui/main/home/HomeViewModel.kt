package com.catasoft.autoclub.ui.main.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.getAvatarDownloadUrl
import com.catasoft.autoclub.repository.CurrentUser
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){

    val avatarUri: MutableLiveData<Uri?> = MutableLiveData()

    fun getAvatarDownloadUri()
    {
        viewModelScope.launch {
            kotlin.runCatching {
                CurrentUser.getEntity().getAvatarDownloadUrl()
            }.onSuccess {
                avatarUri.postValue(it)
            }
        }
    }

}