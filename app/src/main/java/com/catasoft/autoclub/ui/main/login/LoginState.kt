package com.catasoft.autoclub.ui.main.login

import com.google.firebase.auth.FirebaseUser

sealed class LoginState {
    data class Registered(val user: FirebaseUser?) : LoginState()
    object NotRegistered : LoginState()
    object FetchError : LoginState()
}