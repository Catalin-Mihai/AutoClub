package com.catasoft.autoclub.repository

import com.catasoft.autoclub.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

abstract class BaseRepository {

    protected val mUsersCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)

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