package com.catasoft.autoclub.model.user

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.Serializable

data class User(
    var uid: String? = null,
    var numberPlate: String? = null,
    var name : String? = null,
    var joinDate: String? = null,
    var facebookProfile: String? = null
): Serializable

suspend fun User.getAvatarDownloadUrl(): Uri? {
    val ref = "avatar/${this.uid}.jpg"
    Timber.e("Location: $ref")
    return Firebase.storage.reference.child(ref).downloadUrl.await()
}