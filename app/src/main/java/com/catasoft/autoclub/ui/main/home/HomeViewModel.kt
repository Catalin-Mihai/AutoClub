package com.catasoft.autoclub.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){

    val profileName: MutableLiveData<String> = MutableLiveData()

    fun getProfileName()
    {
        viewModelScope.launch {
            val name = CurrentUser.getEntity().name
            profileName.postValue(name)
        }
    }

}