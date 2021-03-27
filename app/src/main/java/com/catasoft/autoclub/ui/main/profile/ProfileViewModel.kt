package com.catasoft.autoclub.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel

@Inject
constructor(
    private val usersRepository: IUsersRepository
) : ViewModel() {

    val userLiveData: MutableLiveData<User> = MutableLiveData();

    fun getUserByUid(uid: String) {
        viewModelScope.launch {
            val user = usersRepository.getUserByUid(uid)
            if(user != null)
                userLiveData.postValue(user)
        }
    }
}