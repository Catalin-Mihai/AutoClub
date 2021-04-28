package com.catasoft.autoclub.model.user

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.Serializable

data class User(
    var uid: String? = null,
    var numberPlate: String? = null,
    var name : String? = null,
    var joinDate: String? = null,
    var facebookProfile: String? = null,
    var postsCount: Int? = null,
    var followersCount: Int? = null,
    var carsCount: Int? = null
): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(numberPlate)
        parcel.writeString(name)
        parcel.writeString(joinDate)
        parcel.writeString(facebookProfile)
        parcel.writeValue(postsCount)
        parcel.writeValue(followersCount)
        parcel.writeValue(carsCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
