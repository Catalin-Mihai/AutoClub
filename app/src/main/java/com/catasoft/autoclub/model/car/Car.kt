package com.catasoft.autoclub.model.car

import android.net.Uri

data class Car(
    var id: String? = null,
    var make: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var numberPlate: String? = null,
    var ownerUid: String? = null
)
