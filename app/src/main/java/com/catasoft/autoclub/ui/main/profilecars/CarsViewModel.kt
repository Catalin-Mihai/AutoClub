package com.catasoft.autoclub.ui.main.profilecars

import androidx.core.net.toUri
import androidx.lifecycle.*
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.CarsRepository
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import com.catasoft.autoclub.util.getAvatarDownloadUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CarsViewModel

@Inject
constructor(
    private val carsRepository: ICarsRepository
) : ViewModel() {

    var dataSource: ArrayList<CarProfileModel> = ArrayList()
    var carsList: MutableLiveData<List<CarProfileModel>> = MutableLiveData()
    var liveCarDeleted: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Timber.e("S-a creat CARS VIEWMODEL!")
    }

    fun getUserCarsList(uid: String){
        viewModelScope.launch {


            val retList = carsRepository.getCarsByUserId(uid).map {
                return@map CarProfileModel(
                    id=it.id,
                    make = it.make,
                    model = it.model,
//                        photoDownloadLink = it.getAvatarDownloadUri()
//                    photoDownloadLink = kotlin.runCatching { it.getAvatarDownloadUri() }.getOrNull()
                    photoDownloadLink = it.avatarUri?.toUri()
                )
            }

            carsList.postValue(retList)
            dataSource.clear()
            dataSource.addAll(retList)
        }
    }

    fun deleteCar(carId: String){
        viewModelScope.launch {
            carsRepository.deleteCar(carId)
            liveCarDeleted.postValue(true)

            var toBeDeletedIndex = -1

            for(i: Int in 0 until dataSource.size){
                if(dataSource[i].id == carId)
                    toBeDeletedIndex = i
            }

            if(toBeDeletedIndex != -1)
                dataSource.removeAt(toBeDeletedIndex)

            val newDataSource = dataSource
            dataSource.clear()
            dataSource.addAll(newDataSource)
            carsList.postValue(dataSource)
        }
    }
}