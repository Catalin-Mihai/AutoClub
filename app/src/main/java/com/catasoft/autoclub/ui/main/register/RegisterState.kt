package com.catasoft.autoclub.ui.main.register

import com.catasoft.autoclub.model.UserRegisterModel

sealed class RegisterState {
    data class Registered(val user: UserRegisterModel?) : RegisterState()
    object Failed : RegisterState()
}