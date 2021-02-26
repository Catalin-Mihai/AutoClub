package com.catasoft.autoclub.model

import java.io.Serializable

data class User(
    var uid: String? = null,
    var numberPlate: String? = null,
    var name : String? = null,
    var joinDate: String? = null,
    var facebookProfile: String? = null
): Serializable