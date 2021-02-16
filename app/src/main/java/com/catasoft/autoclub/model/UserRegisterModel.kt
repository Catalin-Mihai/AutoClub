package com.catasoft.autoclub.model

import java.io.Serializable

data class UserRegisterModel(
    var name: String? = null,
    var numberPlate: String? = null
): Serializable

//Extension map functions

fun UserRegisterModel.toUser() = User (
    name = this.name,
    numberPlate = this.numberPlate
)