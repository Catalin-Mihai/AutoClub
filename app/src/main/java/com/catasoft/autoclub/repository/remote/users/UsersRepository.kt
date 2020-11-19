package com.catasoft.autoclub.repository.remote.users;

import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepository @Inject constructor() : BaseRepository() {

    private val mUsersCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS);

    suspend fun getAllUsers(): List<User>? {
        val snapshot = mUsersCollection.get().await()
        return if (!snapshot.isEmpty) snapshot.toObjects() else null
    }

    fun addUser(user: User) = flow<State<DocumentReference>> {
        emit(State.loading())

        val userRef = mUsersCollection.add(user).await()

        emit(State.success(userRef))
    }

    suspend fun getUserByUid(uid: String): User? {
        val user: User
        val snapshot = mUsersCollection.whereEqualTo("userUid", uid).limit(1).get().await()

        if (snapshot.isEmpty)
            return null

        user = snapshot.first().toObject()
        return user
    }
}