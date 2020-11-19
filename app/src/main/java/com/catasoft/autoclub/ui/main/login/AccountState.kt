package com.catasoft.autoclub.ui.main.login

import com.google.firebase.auth.FirebaseUser

sealed class AccountState {
    data class Registered(val user: FirebaseUser?) : AccountState()
    object NotRegistered : AccountState()
    object FetchError : AccountState()
}