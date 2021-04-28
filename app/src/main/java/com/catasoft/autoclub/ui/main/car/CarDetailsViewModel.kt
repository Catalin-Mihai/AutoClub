package com.catasoft.autoclub.ui.main.car

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.car.CarDetailsModel
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.model.car.toCarDetailsModel
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel

@Inject
constructor(
    private val carsRepository: ICarsRepository,
    private val usersRepository: IUsersRepository
) : ViewModel() {

    var carDetailsModelLive: MutableLiveData<CarDetailsModel> = MutableLiveData()

    fun loadCarDetailsModel(carId: String){

        viewModelScope.launch {
            val carDbModel = carsRepository.getCarById(carId)

            val carDetailsModel = carDbModel?.toCarDetailsModel(usersRepository)
            carDetailsModel?.photosLinks = listOf(carDetailsModel?.avatarLink !!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!,
                carDetailsModel.avatarLink!!)
            carDetailsModelLive.postValue(carDetailsModel)
        }
    }
}