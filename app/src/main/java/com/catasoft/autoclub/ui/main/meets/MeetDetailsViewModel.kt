package com.catasoft.autoclub.ui.main.meets

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.repository.remote.IMeetsRepository
import com.catasoft.autoclub.repository.remote.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetDetailsViewModel

@Inject
constructor(
    private val mUsersRepository: IUsersRepository
): ViewModel(){

    val liveOwner: MutableLiveData<User?> = MutableLiveData()

    fun getOwnerByUid(uid: String?){
        uid?.let{
            viewModelScope.launch {
                liveOwner.postValue(mUsersRepository.getUserByUid(uid))
            }
        }
    }
}