package com.catasoft.autoclub.ui.main.home

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.ICurrentUser
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import com.catasoft.autoclub.ui.main.login.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel

@Inject
constructor(
    private val currentUser: ICurrentUser
): ViewModel(){

    val profileName: MutableLiveData<String> = MutableLiveData()

    fun getProfileName()
    {
        viewModelScope.launch {
            val name = currentUser.getEntity()?.name
            profileName.postValue(name)
        }
    }

}