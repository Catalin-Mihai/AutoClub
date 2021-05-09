package com.catasoft.autoclub.repository.remote

import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.catasoft.autoclub.repository.CurrentUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject

interface IMeetsRepository {
    suspend fun addMeet(meet: Meet): Meet
    suspend fun getAllMeets(): List<Meet>?
}

class MeetsRepository @Inject constructor(): IMeetsRepository, BaseRepository(){

    override suspend fun addMeet(meet: Meet): Meet {
        val docRef = mMeetCollection.add(meet).await()

        val cc = Calendar.getInstance()
        val time = cc.timeInMillis

        docRef.update(
            Constants.MEETS_ID, docRef.id,
            Constants.MEETS_CREATION_TIME, time,
            Constants.MEETS_OWNER_UID, CurrentUser.getUid()
        ).await()
//        Timber.e("INTRE")

        meet.id = docRef.id
        return meet
    }

    override suspend fun getAllMeets(): List<Meet>? {
        val snapshot = mMeetCollection.orderBy(Constants.MEETS_CREATION_TIME, Query.Direction.DESCENDING).get().await()

        if(snapshot.isEmpty)
            return null

        return snapshot.toObjects()
    }

}