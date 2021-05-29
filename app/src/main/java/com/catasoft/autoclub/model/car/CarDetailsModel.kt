package com.catasoft.autoclub.model.car

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.catasoft.autoclub.model.user.User

data class CarDetailsModel(
    var id: String? = null,
    var make: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var numberPlate: String? = null,
    var owner: User? = null,
    var avatarLink: Uri? = null,
    var photosLinks: List<Uri>? = null,
    var description: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.createTypedArrayList(Uri.CREATOR),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeValue(year)
        parcel.writeString(numberPlate)
        parcel.writeParcelable(owner, flags)
        parcel.writeParcelable(avatarLink, flags)
        parcel.writeTypedList(photosLinks)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarDetailsModel> {
        override fun createFromParcel(parcel: Parcel): CarDetailsModel {
            return CarDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<CarDetailsModel?> {
            return arrayOfNulls(size)
        }
    }

}