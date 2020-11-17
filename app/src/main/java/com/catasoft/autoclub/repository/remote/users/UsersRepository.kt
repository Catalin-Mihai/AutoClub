package com.catasoft.autoclub.repository.remote.users;

import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class UsersRepository {

    private val mUsersCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS);

    fun getAllUsers() = flow<State<List<User>>> {

        //Loading state
        emit(State.loading())

        val snapshot = mUsersCollection.get().await()
        val users = snapshot.toObjects(User::class.java)

        //Success state
        emit(State.success(users))
    }.catch {
        //Fail
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO) //another thread (not the main UI thread)

    fun getAllUsers2() = BaseRepository.dbCall<List<User>> {
        suspend {
            val snapshot = mUsersCollection.get().await()
            val users = snapshot.toObjects(User::class.java)
            users
        }
    }

    fun addUser(user: User) = flow<State<DocumentReference>> {
        emit(State.loading())

        val userRef = mUsersCollection.add(user).await()

        emit(State.success(userRef))
    }

/*    fun getUserByUid(uid: String){
        emit(State.loading<>())
    }*/
}