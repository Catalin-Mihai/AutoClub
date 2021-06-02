package com.catasoft.autoclub.model.user

import android.net.Uri
import com.catasoft.autoclub.model.car.Car

data class UserSearchModel(
    var name: String? = null,
    var cars: List<Car>? = null,
    var photoDownloadUrl: Uri? = null,
    var uid: String? = null
)
