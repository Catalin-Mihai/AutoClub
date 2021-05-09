package com.catasoft.autoclub.ui.main.addmeet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.model.meet.MeetDisplayModel
import com.catasoft.autoclub.repository.remote.IMeetsRepository
import com.catasoft.autoclub.repository.remote.MeetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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

    companion object {
        val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    fun saveMeet(){
        viewModelScope.launch {
            liveMeet.value?.meetTime = 123 //TODO: STERGE TEST!
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
                else {
                    liveValidationState.value = false
                    liveValidationMessage.value = "Descrierea nu poate fi mai mare de 100 de caractere!"
                }
            }
        }
    }
}
