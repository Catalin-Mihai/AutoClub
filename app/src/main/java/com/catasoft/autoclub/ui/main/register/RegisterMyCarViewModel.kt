package com.catasoft.autoclub.ui.main.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.model.UserRegisterModel
import com.catasoft.autoclub.model.toUser
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import com.catasoft.autoclub.ui.main.login.LoginState
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class RegisterMyCarViewModel

@Inject
constructor(
    private val usersRepository: IUsersRepository
): ViewModel(){

    val numberPlateAvailable: MutableLiveData<NumberPlateState> = MutableLiveData()
    private val checkChannel = Channel<String>()

    init {

        // Listen for channel values.
        // Check every last request that is sent from the View (with respect to the debounce time)
        viewModelScope.launch {
            checkChannel.consumeAsFlow()
                .debounce(500) //Do not process faster than 500 ms between requests
                .filter { it.isNotBlank() }
                .collect {
                    Timber.e("Channel: %s", it)
                    checkNumberPlate(it)
                }
        }
    }

    fun newCheckRequestFromView(newValue: String)
    {
        viewModelScope.launch {
            checkChannel.send(newValue)
        }
    }

    private fun isValidNumberPlateFormat(license: String): Boolean
    {
        //A valid number looks like this: AG08BAW (exception B300XYZ that can have 3 digits on the middle and has only one letter for the county)
        //There are 6-7 characters

        if (!license.trim().matches("""(?=.{6,7}$)^[A-Z]{1,2}[0-9]{1,3}[A-Z]{1,3}$""".toRegex()))
            return false

        return true
    }

    private suspend fun checkNumberPlate(numberPlate: String)
    {
        //We have a valid user register model. (validated throughout the register steps)
        //So, no more validations

        if(!isValidNumberPlateFormat(numberPlate)){
            numberPlateAvailable.postValue(NumberPlateState.InvalidFormat)
            return
        }

        BaseRepository.singleResultAsStateFlow {
            usersRepository.getUserByNumberPlate(numberPlate)
        }.collect {
            state ->
            when(state){
                is State.Loading -> {
                    numberPlateAvailable.postValue(NumberPlateState.Fetching)
                }
                is State.Success -> {
                    if(state.data == null)
                        numberPlateAvailable.postValue(NumberPlateState.NotUsed)
                    else
                        numberPlateAvailable.postValue(NumberPlateState.Used(state.data))
                }
                is State.Failed -> {
                    numberPlateAvailable.postValue(NumberPlateState.FetchError)
                }
            }
        }

    }

    companion object {
        sealed class NumberPlateState {
            data class Used(val ownedByUser: User) : NumberPlateState()
            object NotUsed : NumberPlateState()
            object Fetching: NumberPlateState()
            object FetchError: NumberPlateState()
            object InvalidFormat: NumberPlateState()
        }
    }
}