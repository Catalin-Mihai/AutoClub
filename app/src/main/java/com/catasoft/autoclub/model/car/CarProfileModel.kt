package com.catasoft.autoclub.model.car

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.Serializable

data class CarProfileModel(
    var id: String?,
    var make: String?,
    var model: String?,
    var photoDownloadLink: Uri?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeParcelable(photoDownloadLink, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarProfileModel> {
        override fun createFromParcel(parcel: Parcel): CarProfileModel {
            return CarProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<CarProfileModel?> {
            return arrayOfNulls(size)
        }
    }

}
