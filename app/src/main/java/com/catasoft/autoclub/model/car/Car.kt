package com.catasoft.autoclub.model.car

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Car(
    var id: String? = null,
    var make: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var numberPlate: String? = null,
    var ownerUid: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeValue(year)
        parcel.writeString(numberPlate)
        parcel.writeString(ownerUid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}
