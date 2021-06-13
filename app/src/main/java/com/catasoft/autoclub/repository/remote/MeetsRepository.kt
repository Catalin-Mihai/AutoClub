package com.catasoft.autoclub.repository.remote

import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.util.getCurrentTimeInMillis
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

        val time = getCurrentTimeInMillis()

        docRef.update(
            Constants.MEETS_ID, docRef.id,
            Constants.MEETS_CREATION_TIME, time,
            Constants.MEETS_OWNER_UID, CurrentUser.getUid()
        ).await()
        meet.id = docRef.id

        meet.ownerUid = CurrentUser.getUid()
        //update user meets number
        if(meet.ownerUid != null){
            val ownerDoc = mUsersCollection.whereEqualTo(Constants.USERS_UID, meet.ownerUid).get().await().documents[0]
            val dbVal = ownerDoc.get(Constants.USERS_MEETS_COUNT)

            var currentMeetsCount: Int = 0
            if(dbVal != null)
                currentMeetsCount = (dbVal as Long).toInt()

            Timber.e("Meets count: %s", currentMeetsCount)
            ownerDoc.reference.update(Constants.USERS_MEETS_COUNT, currentMeetsCount + 1).await()
        }

        return meet
    }

    override suspend fun getAllMeets(): List<Meet>? {
        val snapshot = mMeetCollection.orderBy(Constants.MEETS_CREATION_TIME, Query.Direction.DESCENDING).get().await()

        if(snapshot.isEmpty)
            return null

        return snapshot.toObjects()
    }

}