package com.catasoft.autoclub.ui.main.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.model.user.UserRegisterModel
import com.catasoft.autoclub.model.user.toUser
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterMyProfileViewModel

@Inject
constructor(
    private val usersRepository: IUsersRepository
): ViewModel(){

    val registerState: MutableLiveData<RegisterState> = MutableLiveData()

    fun registerUser(userRegisterModel: UserRegisterModel)
    {
        //We have a valid user register model. (validated throughout the register steps)
        //So, no more validations

        viewModelScope.launch {

            kotlin.runCatching {
                val userRef = usersRepository.addUser(userRegisterModel.toUser())
                val documentSnapshot = userRef.get().await()
                val user: User? = documentSnapshot.toObject()
                Timber.e("%s - %s", user!!.name, user.uid)

            }
            .onSuccess {
                registerState.postValue(RegisterState.Registered(userRegisterModel))
            }
            .onFailure {
                registerState.postValue(RegisterState.Failed)
            }

        }
    }
}