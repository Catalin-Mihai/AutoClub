package com.catasoft.autoclub.ui.main.register

import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.model.user.UserRegisterModel
import com.catasoft.autoclub.model.user.toUser
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel

@Inject
constructor(
    private val usersRepository: IUsersRepository
): ViewModel(){

    val registerState: MutableLiveData<RegisterState> = MutableLiveData()
    val liveUserRegisterModel: MutableLiveData<UserRegisterModel> = MutableLiveData(UserRegisterModel())

    val liveValidationState: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveValidationMessage: MutableLiveData<String> = MutableLiveData()

    fun addUserName(userName: String){
        liveUserRegisterModel.value?.name = userName
        liveUserRegisterModel.value = liveUserRegisterModel.value
    }

    fun registerUserInDatabase(defaultAvatar: Bitmap? = null)
    {
        viewModelScope.launch {

            kotlin.runCatching {
                val userRef = usersRepository.addUser(liveUserRegisterModel.value?.toUser()!!)
                /*val documentSnapshot = userRef.get().await()
                val user: User? = documentSnapshot.toObject()
                Timber.e("%s - %s", user!!.name, user.uid)*/

                defaultAvatar?.let {
                    usersRepository.setAvatar(CurrentUser.getUid()!!, it)
                }
            }
                .onSuccess {
                    registerState.postValue(RegisterState.Registered(liveUserRegisterModel.value))
                }
                .onFailure {
                    registerState.postValue(RegisterState.Failed)
                }

        }
    }

    fun validatePage(position: Int){
        when(position){
            0 -> {
                val name = liveUserRegisterModel.value?.name
                if(name == null){
                    liveValidationState.value = false
                    liveValidationMessage.value = "Nume invalid!"
                    return
                }
                if(name.length < 4 || name.length > 30){
                    liveValidationState.value = false
                    liveValidationMessage.value = "Numele trebuie să aibă între 4 și 30 de caractere"
                    return
                }
                liveValidationState.value = true
                liveValidationMessage.value = null
            }
        }
    }
}