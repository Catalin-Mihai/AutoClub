package com.catasoft.autoclub.repository

import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

interface ICurrentUser {

    suspend fun getEntity(): User?
    suspend fun getUid(): String?
    suspend fun updateByMerging(user: User)
}

class CurrentUser @Inject constructor(
    private val userRepository: IUsersRepository
): ICurrentUser{

    private val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getEntity(): User? {

        if(currentFirebaseUser == null)
            return null

        return userRepository.getUserByUid(currentFirebaseUser.uid)
    }

    override suspend fun getUid(): String? {
        return currentFirebaseUser?.uid
    }

    override suspend fun updateByMerging(user: User) {
        return userRepository.updateByMerging(user)
    }

}