package com.catasoft.autoclub.ui.main.login

import com.google.firebase.auth.FirebaseUser

sealed class AccountState(val user: FirebaseUser?) {
    data class Registered(val u: FirebaseUser?) : AccountState(u)
    data class NotRegistered(val u: FirebaseUser?) : AccountState(u)
    data class FetchError(val u: FirebaseUser?) : AccountState(u)
}