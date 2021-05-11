package com.catasoft.autoclub.ui.main.addmeet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.repository.remote.IMeetsRepository
import com.catasoft.autoclub.util.dateAndTimeToMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddMeetViewModel

@Inject
constructor(
    val mMeetsRepository: IMeetsRepository
): ViewModel()
{
    // TODO: Implement the ViewModel

    val liveMeet: MutableLiveData<Meet> = MutableLiveData(Meet())
    val liveValidationState: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveValidationMessage: MutableLiveData<String> = MutableLiveData()
    val liveMeetSaved: MutableLiveData<Boolean> = MutableLiveData(false)

    val liveDateCalendar: MutableLiveData<Calendar> = MutableLiveData()
    val liveTimeCalendar: MutableLiveData<Calendar> = MutableLiveData()

/*
    private var meetYear: Int? = null
    private var meetMonthOfYear: Int? = null
    private var meetDayOfMonth: Int? = null
    private var meetHourOfDay: Int? = null
    private var meetMinute: Int? = null
*/

    companion object {
        val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    private fun updateMeetMillis(){

        liveDateCalendar.value?.let { date ->
            liveTimeCalendar.value?.let { time ->

                val year = date.get(Calendar.YEAR)
                val month = date.get(Calendar.MONTH)
                val day = date.get(Calendar.DAY_OF_MONTH)
                val hour = time.get(Calendar.HOUR_OF_DAY)
                val minutes = time.get(Calendar.MINUTE)

                liveMeet.value?.meetTime = dateAndTimeToMillis(
                    year, month, day, hour, minutes
                )

                liveMeet.value = liveMeet.value
            }
        }

    }

    fun setDate(year: Int, monthOfYear: Int, dayOfMonth: Int){
/*        meetYear = year
        meetMonthOfYear = monthOfYear
        meetDayOfMonth = dayOfMonth*/

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        liveDateCalendar.value = calendar
        updateMeetMillis()
    }

    fun setTime(hourOfDay: Int, minute: Int){
/*
        meetHourOfDay = hourOfDay
        meetMinute = minute
*/

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        liveTimeCalendar.value = calendar

        updateMeetMillis()
    }

    fun saveMeet(){
        viewModelScope.launch {
//            Timber.e("INAINTE")
            mMeetsRepository.addMeet(liveMeet.value!!)
//            Timber.e("DUPA")
            liveMeetSaved.postValue(true)
        }
    }

    fun validatePage(position: Int){
        when(position){
            0 -> {
                if(liveMeet.value?.placeId != null){
                    liveValidationState.value = true
                }
                else liveValidationMessage.value = "Selectati un loc"
            }
            1 -> {
                if(liveMeet.value?.description != null && liveMeet.value?.description!!.length < 100){
                    liveValidationState.value = true
                }
                else if(liveMeet.value?.description == null){
                    liveValidationState.value = true
                }
                else {
                    liveValidationState.value = false
                    liveValidationMessage.value = "Descrierea nu poate fi mai mare de 100 de caractere!"
                }
            }
            2 -> {
                if(liveMeet.value?.meetTime != null)
                    liveValidationState.value = true
                else {
                    liveValidationState.value = false
                    liveValidationMessage.value = "Selectati data si ora"
                }
            }
        }
    }
}
