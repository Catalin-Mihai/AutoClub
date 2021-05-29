package com.catasoft.autoclub.model.car

import android.net.Uri

data class CarPhotoModel (
    var carId: String? = null,
    var description: String? = null,
    var photoUri: Uri? = null
)