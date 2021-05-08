package com.catasoft.autoclub.ui.main.profilesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.model.user.UserSearchModel
import com.catasoft.autoclub.repository.remote.CarsRepository
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.util.getAvatarDownloadUri
import com.catasoft.autoclub.repository.remote.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class ProfileSearchViewModel

@Inject
constructor(
    private val mUsersRepository: IUsersRepository,
    private val mCarsRepository: ICarsRepository
) : ViewModel() {

    val usersLiveData: MutableLiveData<List<UserSearchModel>> = MutableLiveData()
    private val inputChannel = Channel<String?>()

    init {
        viewModelScope.launch {
            inputChannel.consumeAsFlow()
                .debounce(500) //Do not process faster than 500 ms between requests
                .filter { !it.isNullOrBlank() }
                .collect {
                    Timber.e("Channel: %s", it)
                    getUsersByInput(it!!)
                }
        }
    }

    fun pushInput(input: String?) {
        viewModelScope.launch {
            inputChannel.send(input)
        }
    }

    private fun getUsersByInput(input: String) {
        viewModelScope.launch {
            val dbUsers = mUsersRepository.getUsersByPartialName(input)
            //Debug
//            users.let {
//                for(user in it) {
//                    Timber.e("%s", user)
//                }
//            }

            dbUsers?.let { users ->
                usersLiveData.postValue(users.map {

                    val userSearchModel = UserSearchModel()
                    userSearchModel.name = it.name
                    userSearchModel.photoDownloadUrl = kotlin.runCatching {
                        it.getAvatarDownloadUri()
                    }.onFailure {
                        Timber.e("Invalid avatar location!")
                    }.getOrNull()

                    val cars: List<Car> = mCarsRepository.getCarsByUserId(it.uid!!)

                    userSearchModel.cars = cars
                    return@map userSearchModel
                })
            }

        }
    }

}