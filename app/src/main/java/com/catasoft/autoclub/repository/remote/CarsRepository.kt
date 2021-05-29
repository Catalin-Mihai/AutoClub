package com.catasoft.autoclub.repository.remote

import android.graphics.Bitmap
import android.net.Uri
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.util.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface ICarsRepository {
    suspend fun addCar(car: Car): Car
    suspend fun setAvatar(id: String, photo: Bitmap)
    suspend fun getCarsByUserId(uid: String): List<Car>
    suspend fun getCarsByUserIdAsFlow(uid: String): Flow<State<List<Car>>>
    suspend fun getCarById(id: String): Car?
    suspend fun addPhoto(carId: String, bitmap: Bitmap)
    suspend fun addDescription(carId: String, description: String?)
    suspend fun deletePhoto(carId: String, photoUri: String)
}

@ExperimentalCoroutinesApi
class CarsRepository @Inject constructor(): ICarsRepository, BaseRepository(){

    override suspend fun addCar(car: Car): Car {
        val docRef = mCarsCollection.add(car).await()
        docRef.update(Constants.CARS_ID, docRef.id).await()
        car.id = docRef.id

        //update user cars number
        if(car.ownerUid != null){
            val ownerDoc = mUsersCollection.whereEqualTo(Constants.USERS_UID, car.ownerUid).get().await().documents[0]
            val currentCarsCount: Int = (ownerDoc.get(Constants.CARS_COUNT) as Long).toInt()
            Timber.e("Cars count: %s", currentCarsCount)
            ownerDoc.reference.update(Constants.CARS_COUNT,  currentCarsCount + 1).await()
        }
        return car
    }

    override suspend fun setAvatar(id: String, photo: Bitmap){

        val storageUserPhotoRef = Firebase.storage.reference.child("cars/${id}/avatar.jpg")
        uploadPhotoToFirestore(storageUserPhotoRef, photo)

        //Add the link to the car entity for faster access
        val docRef = mCarsCollection.whereEqualTo(Constants.CARS_ID, id).get().await().documents[0].reference
        val uri = getCarAvatarDownloadUri(id)
        docRef.update(Constants.CARS_AVATAR_URI, uri.toString())
    }

    override suspend fun getCarsByUserId(uid: String): List<Car> {
        val snapshot = mCarsCollection.whereEqualTo(Constants.CARS_OWNER_UID, uid).get().await()
        return snapshot.toObjects()
    }

    override suspend fun getCarsByUserIdAsFlow(uid: String): Flow<State<List<Car>>> =
        callbackFlow {
            val realTimeListener = mCarsCollection.whereEqualTo(Constants.CARS_OWNER_UID, uid)
                .addSnapshotListener{ snapshot, exception ->
                    snapshot?.let { snap ->
                        offer(State.success(snap.toObjects(Car::class.java)))
                    }

                    // If exception occurs, cancel this scope with exception message.
                    exception?.let {
                        offer(State.failed(it.message.toString()))
                        cancel(it.message.toString())
                    }
                }

            awaitClose {
                // This block is executed when producer channel is cancelled
                // This function resumes with a cancellation exception.

                // Dispose listener
                realTimeListener.remove()
                this.cancel()
            }
        }



    override suspend fun getCarById(id: String): Car? {
        val snapshot = mCarsCollection.whereEqualTo(Constants.CARS_ID, id).limit(1).get().await()

        if (snapshot.isEmpty)
            return null

        return snapshot.first().toObject()
    }

    override suspend fun addPhoto(carId: String, bitmap: Bitmap) {
        val storageUserPhotoRef = Firebase.storage.reference
            .child("cars/${carId}/${getCurrentTimeInMillis()}")

        uploadPhotoToFirestore(storageUserPhotoRef, bitmap)

        //Add the links to the car entity for faster access
        val docRef = mCarsCollection.whereEqualTo(Constants.CARS_ID, carId).get().await().documents[0].reference
        val newPhotosArray = getAllCarPhotosDownloadUri(carId)

        newPhotosArray?.let {
            Timber.e(it.toString())
            docRef.update(Constants.CARS_PHOTOS_URI_ARRAY, newPhotosArray)
        }
    }

    override suspend fun deletePhoto(carId: String, photoUri: String) {
        //Remove the link from the array
        val document = mCarsCollection.whereEqualTo(Constants.CARS_ID, carId).get().await().documents[0]
        val docRef = document.reference
        val data: Car? = document.toObject()

        if(data != null){
            val oldUris = data.photosUri
            oldUris?.let {
                val newUris = (it as ArrayList)
                newUris.remove(photoUri)
                docRef.update(Constants.CARS_PHOTOS_URI_ARRAY, newUris)
            }
            //Delete the file from storage
            deleteCarPhotoByUri(carId, photoUri)
        }
    }

    override suspend fun addDescription(carId: String, description: String?) {
        val carDocRef = mCarsCollection.whereEqualTo(Constants.CARS_ID, carId).get().await().documents[0].reference
        carDocRef.update(Constants.CARS_DESCRIPTION, description)
    }
}