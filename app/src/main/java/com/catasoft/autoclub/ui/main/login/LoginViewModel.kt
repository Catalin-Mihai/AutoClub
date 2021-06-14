package com.catasoft.autoclub.ui.main.login

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.IUsersRepository
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
class LoginViewModel

@Inject
constructor(
    private val usersRepository: IUsersRepository
): ViewModel(){

    private var firebaseAuth: FirebaseAuth = Firebase.auth
    val loginState: MutableLiveData<LoginState> = MutableLiveData()

    fun signInWithGoogle(resultData: Intent?) {
        viewModelScope.launch { //Lansat in Main Thread
            kotlin.runCatching {

                //Lansat in IO thread
                val account = GoogleSignIn.getSignedInAccountFromIntent(resultData).await()

                //Asteptam raspunsul
                val credential = GoogleAuthProvider.getCredential(account?.idToken!!, null)

                //Lansat in IO thread
                firebaseAuth.signInWithCredential(credential).await()
            }.onSuccess {
                Timber.e("signInWithCredential:success")
                val user = firebaseAuth.currentUser
                handlePostLogin(user)
            }.onFailure {
                Timber.e("Google sign in with firebase:failure: %s", it.message.toString())
                handlePostLogin(null)
            }
        }
    }

    private fun handlePostLogin(user: FirebaseUser?) {
        if(user == null) {
            loginState.postValue(LoginState.FetchError)
            return
        }
        viewModelScope.launch {
            BaseRepository.singleResultAsStateFlow {
                usersRepository.getUserByUid(user.uid)
            }
            .collect { state ->
                when (state) {
                    is State.Loading -> {
                        Timber.e("Loading...")
                    }
                    is State.Success -> {
                        Timber.e("Date: ")
                        Timber.e(state.data.toString())
                        if(state.data == null)
                            loginState.postValue(LoginState.NotRegistered)
                        else
                            loginState.postValue(LoginState.Registered(user))
                    }
                    is State.Failed -> {
                        Timber.e("Eroare GET1%s", state.message)
                        loginState.postValue(LoginState.FetchError)
                    }
                }
            }
        }
    }

}