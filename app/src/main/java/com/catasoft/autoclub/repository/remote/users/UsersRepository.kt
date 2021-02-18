package com.catasoft.autoclub.repository.remote.users

import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface IUsersRepository {

    suspend fun getUserByUid(uid: String): User?
    suspend fun getAllUsers(): List<User>?
    suspend fun addUser(user: User): DocumentReference
    suspend fun getUserByNumberPlate(numberPlate: String): User?

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

        docRef.update(
            Constants.USERS_UID, currentAuthUser.uid
        )

        return docRef
    }

    override suspend fun getUserByUid(uid: String): User? {
        val snapshot = mUsersCollection.whereEqualTo(Constants.USERS_UID, uid).limit(1).get().await()

        if (snapshot.isEmpty)
            return null

        return snapshot.first().toObject()
    }

    override suspend fun getUserByNumberPlate(numberPlate: String): User? {
        val users = mUsersCollection.whereEqualTo(Constants.USERS_NUMBER_PLATE, numberPlate).limit(1)
        val snapshot = users.get().await()

        if(snapshot.isEmpty)
            return null

        return snapshot.first().toObject()
    }
}