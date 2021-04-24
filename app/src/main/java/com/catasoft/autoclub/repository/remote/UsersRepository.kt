package com.catasoft.autoclub.repository.remote

import android.graphics.Bitmap
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

interface IUsersRepository {

    suspend fun getUserByUid(uid: String): User?
    suspend fun getAllUsers(): List<User>?
    suspend fun addUser(user: User): DocumentReference
    suspend fun getUserByNumberPlate(numberPlate: String): User?
    suspend fun updateByMerging(user: User)
    suspend fun getUserDocumentByUid(uid: String): DocumentReference?
    suspend fun setAvatar(uid: String, photo: Bitmap)
    suspend fun getUsersByPartialName(partialName: String): List<User>?
}

class UsersRepository @Inject constructor(): IUsersRepository, BaseRepository() {

    override suspend fun getAllUsers(): List<User>? {
        val snapshot = mUsersCollection.get().await()
        return if (!snapshot.isEmpty) snapshot.toObjects() else null
    }

    override suspend fun addUser(user: User): DocumentReference {
        val docRef = mUsersCollection.add(user).await()

/**
 * NOT DESIRED
        Add the uid of the document reference to the uid user model field (Might be useful for querying)

        docRef.update(
            Constants.USERS_UID, docRef.id
        )
**/

        //Add the auth id of the user to the uid user model field (Useful for querying)
        val currentAuthUser = FirebaseAuth.getInstance().currentUser
            ?: throw Exception("User is not logged. Cannot get the authId when registering account")

        //User might be null (using this method when user is not logged with google)
        //Throw an exception for this

        val cc = Calendar.getInstance()
        val year = cc[Calendar.YEAR]
        val month = cc[Calendar.MONTH]
        val mDay = cc[Calendar.DAY_OF_MONTH]

        docRef.update(
            Constants.USERS_UID, currentAuthUser.uid,
            Constants.USERS_JOIN_DATE, "$mDay-$month-$year"
        )

        return docRef
    }

    override suspend fun getUserByUid(uid: String): User? {
        val snapshot = mUsersCollection.whereEqualTo(Constants.USERS_UID, uid).limit(1).get().await()

        if (snapshot.isEmpty)
            return null

        return snapshot.first().toObject()
    }

    override suspend fun getUserDocumentByUid(uid: String): DocumentReference? {
        return mUsersCollection.whereEqualTo(Constants.USERS_UID, uid).get().await().documents.firstOrNull()?.reference;
    }

    override suspend fun getUserByNumberPlate(numberPlate: String): User? {
        val users = mUsersCollection.whereEqualTo(Constants.USERS_NUMBER_PLATE, numberPlate).limit(1)
        val snapshot = users.get().await()

        if(snapshot.isEmpty)
            return null

        return snapshot.first().toObject()
    }

    override suspend fun updateByMerging(user: User) {

        val docRef = getUserDocumentByUid(user.uid!!)!!

        //create field if they not exists
        docRef.set(user, SetOptions.merge())
    }

    override suspend fun setAvatar(uid: String, photo: Bitmap){

        val storageUserPhotoRef = Firebase.storage.reference.child("avatar/${uid}.jpg")
        uploadPhotoToFirestore(storageUserPhotoRef, photo)
    }

    override suspend fun getUsersByPartialName(partialName: String): List<User>? {

        val snapshot = mUsersCollection.whereGreaterThanOrEqualTo(Constants.USERS_NAME, partialName)
            .whereLessThanOrEqualTo(Constants.USERS_NAME, partialName + "\uf8ff").limit(10).get().await()

        if(!snapshot.isEmpty)
            return snapshot.toObjects()
        return null
    }
}