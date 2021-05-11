package com.catasoft.autoclub.model.user

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class User(
    var uid: String? = null,
    var numberPlate: String? = null,
    var name : String? = null,
    var joinDate: String? = null,
    var facebookProfile: String? = null,
    var postsCount: Int? = null,
    var followersCount: Int? = null,
    var carsCount: Int? = null,
    var normalizedName: String? = null
): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
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
        parcel.writeString(normalizedName)
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
