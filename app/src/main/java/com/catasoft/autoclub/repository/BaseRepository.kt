package com.catasoft.autoclub.repository

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

abstract class BaseRepository {

    protected val mUsersCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)

    protected val mCarsCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_CARS)

    protected suspend fun uploadPhotoToFirestore(photoStorageRef: StorageReference, photo: Bitmap) {
        val baos = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        photoStorageRef.putBytes(data).await()
    }

    companion object {
        fun <R> singleResultAsStateFlow(f: suspend () -> R): Flow<State<R>> =
            flow {
                emit(State.loading())
                val retVal: R = f()
                emit(State.success(retVal))
            }.catch {
                emit(State.failed(it.message.toString()))
            }.flowOn(Dispatchers.IO)
    }
}