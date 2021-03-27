package com.catasoft.autoclub.model.car

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber

data class CarCreationModel (
    var make: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var numberPlate: String? = null,
    var photo: Bitmap? = null
)

fun CarCreationModel.toCarModel(): Car {
    return Car(
        make = make,
        model = model,
        year = year,
        numberPlate = numberPlate
    )
}