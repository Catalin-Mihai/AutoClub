package com.catasoft.autoclub.repository.remote

import android.graphics.Bitmap
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.catasoft.autoclub.util.getAvatarDownloadUri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface ICarsRepository {
    suspend fun addCar(car: Car): Car
    suspend fun setAvatar(id: String, photo: Bitmap)
    suspend fun getCarsByUserId(uid: String): List<Car>
}

class CarsRepository @Inject constructor(): ICarsRepository, BaseRepository(){
    override suspend fun addCar(car: Car): Car {
        val docRef = mCarsCollection.add(car).await()
        docRef.update(Constants.CARS_ID, docRef.id).await()
        car.id = docRef.id
        return car
    }

    override suspend fun setAvatar(id: String, photo: Bitmap){

        val storageUserPhotoRef = Firebase.storage.reference.child("cars/${id}/avatar.jpg")
        uploadPhotoToFirestore(storageUserPhotoRef, photo)
    }

    override suspend fun getCarsByUserId(uid: String): List<Car> {
        val snapshot = mCarsCollection.whereEqualTo(Constants.CARS_OWNER_UID, uid).get().await()
        return snapshot.toObjects()
    }
}