package com.catasoft.autoclub.util

import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
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
import java.lang.Exception

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()

suspend fun User.getAvatarDownloadUri(): Uri? {
    return getUserAvatarUri(this.uid!!)
}

suspend fun Car.getAvatarDownloadUri(): Uri? {
    return try{
        val ref = "cars/${this.id}/avatar.jpg"
        Timber.e("Car avatar location: $ref")
        Firebase.storage.reference.child(ref).downloadUrl.await()
    }
    catch (e: Exception){
        null
    }
}

suspend fun Car.getAllPhotosDownloadUri(): List<Uri>?{
    val refUrl = "cars/${this.id}"
    val listRes = Firebase.storage.reference.child(refUrl).listAll().await()
    val photosLinks: ArrayList<Uri> = ArrayList()

    listRes.items.forEach{ item ->
        val link = item.downloadUrl.await()
        photosLinks.add(link)
    }

    if(photosLinks.count() == 0)
        return null

    val list = photosLinks.map { it.toString() }.sortedDescending().map { it.toUri() }
    list.forEach { Timber.e(it.toString()) }
    return list
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