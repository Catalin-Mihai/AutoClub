package com.catasoft.autoclub.ui.main.cardetails

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.car.CarDetailsModel
import com.catasoft.autoclub.model.car.toCarDetailsModel
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class CarDetailsViewModel

@Inject
constructor(
    private val carsRepository: ICarsRepository,
    private val usersRepository: IUsersRepository

) : ViewModel() {

//    val livePhotoAdded: MutableLiveData<Bitmap> = MutableLiveData()
    var carDetailsModelLive: MutableLiveData<CarDetailsModel> = MutableLiveData()

    fun addPhoto(bitmap: Bitmap, carId: String){
        viewModelScope.launch {
            carsRepository.addPhoto(carId, bitmap)
//            livePhotoAdded.postValue(bitmap)
        }.invokeOnCompletion {
            loadCarDetailsModel(carId)
        }
    }

    fun loadCarDetailsModel(carId: String){

        viewModelScope.launch {
            val carDbModel = carsRepository.getCarById(carId)

            val carDetailsModel = carDbModel?.toCarDetailsModel(usersRepository)
            carDetailsModelLive.postValue(carDetailsModel)
        }

    }

    fun deleteCarPhoto(carId: String, uri: Uri){
        viewModelScope.launch {
            carsRepository.deletePhoto(carId, uri.toString())
        }.invokeOnCompletion {
            loadCarDetailsModel(carId)
        }
    }

    fun saveDescription(text: String, carId: String) {
        viewModelScope.launch {
            carsRepository.addDescription(carId, text)
        }.invokeOnCompletion {
            carDetailsModelLive.value?.description = text
            carDetailsModelLive.value = carDetailsModelLive.value
        }
    }
}