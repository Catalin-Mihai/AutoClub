package com.catasoft.autoclub.ui.main.feed;

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.util.getAvatarDownloadUri
import kotlinx.coroutines.launch

class FeedViewModel: ViewModel() {
    val userAvatarUriLive: MutableLiveData<Uri> = MutableLiveData()

    init {
        viewModelScope.launch {
            userAvatarUriLive.postValue(CurrentUser.getEntity().getAvatarDownloadUri())
        }
    }
}
