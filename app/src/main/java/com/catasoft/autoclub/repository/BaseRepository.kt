package com.catasoft.autoclub.repository

import com.catasoft.autoclub.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object BaseRepository {

    fun <R> dbCall(f: () -> suspend () -> R) =
        flow<State<R>> {
            emit(State.loading())
            val retVal:R = f().invoke()
            emit(State.success(retVal))
        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}