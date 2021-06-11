package com.catasoft.autoclub.util

import android.net.Uri
import android.view.View
import com.catasoft.autoclub.repository.CurrentUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.InputStream
import java.util.*

suspend fun getUserAvatarUri(userUid: String): Uri? {
    return try{
        val ref = "avatar/${userUid}.jpg"
        Timber.e("Location: $ref")
        val childRef = Firebase.storage.reference.child(ref)
        childRef.downloadUrl.await()
    }
    catch (e: Exception){
        null
    }
}

fun getUserActionsVisibility(userUid: String?, goneView: Boolean = true): Int {

    var retVal = View.INVISIBLE
    if(goneView) retVal = View.GONE

    if(userUid == null)
        return retVal

    if(isCurrentUser(userUid))
        return View.VISIBLE

    return retVal
}

fun isCurrentUser(userUid: String): Boolean {
    return userUid == CurrentUser.getUid()
}

suspend fun deleteCarPhotoByUri(carId: String, uri: String){
    runCatching {
        val refUrl = "cars/${carId}"
        val listRes = Firebase.storage.reference.child(refUrl).listAll().await()
        val photosLinks: ArrayList<Uri> = ArrayList()

        listRes.items.forEach { item ->
            val link = item.downloadUrl.await()
            if(link.toString() == uri){
                //This item is the one!
                item.delete().await()
                return@forEach
            }
        }
    }
}

suspend fun getCarAvatarDownloadUri(carId: String): Uri? {
    return try{
        val ref = "cars/${carId}/avatar.jpg"
        Timber.e("Car avatar location: $ref")
        Firebase.storage.reference.child(ref).downloadUrl.await()
    }
    catch (e: Exception){
        null
    }
}

suspend fun getAllCarPhotosDownloadUri(carId: String): List<String>? {
    val refUrl = "cars/${carId}"
    val listRes = Firebase.storage.reference.child(refUrl).listAll().await()
    val photosLinks: ArrayList<Uri> = ArrayList()

    listRes.items.forEach { item ->
        if(item.name != "avatar.jpg"){
            val link = item.downloadUrl.await()
            photosLinks.add(link)
        }
    }

    if (photosLinks.count() == 0)
        return null

    return photosLinks.map { it.toString() }.sortedDescending()
}

fun getCurrentTimeInMillis(): Long {
    val cc = Calendar.getInstance()
    return cc.timeInMillis
}

fun takeFirstNLetters(str: String?, n: Int): String?{

    if(str == null) return null
    Timber.e("$str $n")
    if(str.length > n){
        return str.substring(0, n) + "..."
    }
    return str
}

fun formatMillisToDate(millis: Long?): String {

    if(millis == null)
        return "Data nesetata"

    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return "$year-$month-$day"
}

fun formatMillisToDateAndTime(millis: Long?): String {

    if(millis == null)
        return "Data si ora nesetata"

    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)

    return "$year-$month-$day $hour:$minutes"
}

fun formatMillisToTime(millis: Long?): String {

    if(millis == null)
        return "Ora nesetata"

    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)

    return "$hour:$minutes"
}

fun dateAndTimeToMillis(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minute: Int
): Long {

    val calendar: Calendar = Calendar.getInstance()
    calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
    return calendar.timeInMillis
}