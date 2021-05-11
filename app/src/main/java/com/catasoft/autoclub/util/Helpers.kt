package com.catasoft.autoclub.util

import java.util.*

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