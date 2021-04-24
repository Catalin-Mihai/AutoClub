package com.catasoft.autoclub.util

import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.model.user.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}


suspend fun User.getAvatarDownloadUri(): Uri? {
    val ref = "avatar/${this.uid}.jpg"
    Timber.e("Location: $ref")
    val childRef = Firebase.storage.reference.child(ref)
    return childRef.downloadUrl.await()
}

suspend fun Car.getAvatarDownloadUri(): Uri? {
    val ref = "cars/${this.id}/avatar.jpg"
    Timber.e("Car avatar location: $ref")
    return Firebase.storage.reference.child(ref).downloadUrl.await()
}

fun TextInputLayout.showSuccessEndIcon() {
    this.endIconMode = TextInputLayout.END_ICON_CUSTOM

    val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_check_circle_24px)
    drawable?.setTint(ResourcesCompat.getColor(resources, R.color.green_600, null))
    this.endIconDrawable = drawable
}

fun TextInputLayout.hideEndIcon(){
    this.endIconMode = TextInputLayout.END_ICON_NONE
}

fun TextInputLayout.resetFeedback(){
    this.endIconMode = TextInputLayout.END_ICON_NONE
    this.error = null
}