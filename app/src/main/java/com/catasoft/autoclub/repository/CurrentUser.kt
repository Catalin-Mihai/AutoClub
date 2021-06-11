package com.catasoft.autoclub.repository

import com.catasoft.autoclub.model.user.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import timber.log.Timber

interface ICurrentUser {

    fun getEntity(): User?
    fun getUid(): String?
}

object CurrentUser: ICurrentUser {

    private val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    private var userData = User()
    private var initiated = false

    init {
        initiate {  }
    }

    fun initiate(callback: () -> Unit = {}){
        if (currentFirebaseUser != null && !initiated) {

            Timber.e("SUNT CREAT ACUM@@@@@@@@@@@@@@@@@@@@@!")

            Timber.e(currentFirebaseUser.uid)

            val query = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                .whereEqualTo(Constants.USERS_UID, currentFirebaseUser.uid).limit(1)

            //Get the initial user
            query.get().addOnSuccessListener {
                if(!it.isEmpty){
                    userData = it.first().toObject()
                    val userDoc = it.documents.first().reference

                    //Add a data listener to keep the user forever updated
                    userDoc.addSnapshotListener{snapshot, e ->

                        if (e != null) {
                            Timber.e("Listen failed. $e")
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Timber.e("Current data: ${snapshot.data}")
                            userData = snapshot.toObject()!!
                            initiated = true
                        } else {
                            Timber.e("Current data: null")
                        }
                    }
                }
                callback()
            }
        }
        else {
            callback()
        }
    }

    override fun getEntity(): User {
        return userData
    }

    fun isUserConnected(): Boolean
    {
        if (currentFirebaseUser?.uid != null)
            return true

        return false
    }

    override fun getUid(): String? {
        return currentFirebaseUser?.uid
    }


}