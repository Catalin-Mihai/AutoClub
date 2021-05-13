package com.catasoft.autoclub.model.car

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.catasoft.autoclub.repository.remote.CarsRepository
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import com.catasoft.autoclub.util.getAllPhotosDownloadUri
import com.catasoft.autoclub.util.getAvatarDownloadUri

data class Car(
    var id: String? = null,
    var make: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var numberPlate: String? = null,
    var ownerUid: String? = null,
    var avatarUri: String? = null,
    var photosUri: List<String>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeValue(year)
        parcel.writeString(numberPlate)
        parcel.writeString(ownerUid)
        parcel.writeString(avatarUri)
        parcel.writeStringList(photosUri)
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

suspend fun Car.toCarDetailsModel(usersRepository: IUsersRepository): CarDetailsModel {
    val carDetailsModel =  CarDetailsModel(
        id = id,
        make = make,
        model = model,
        year = year,
        numberPlate = numberPlate,
        photosLinks = photosUri?.map { Uri.parse(it) },
        avatarLink = avatarUri?.let { Uri.parse(it) }
    )

    val userId = ownerUid
    if(userId != null)
        carDetailsModel.owner = usersRepository.getUserByUid(userId)

//    carDetailsModel.photosLinks = this.getAllPhotosDownloadUri()
//    carDetailsModel.avatarLink = this.getAvatarDownloadUri()

    return carDetailsModel
}