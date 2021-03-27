package com.catasoft.autoclub.ui.main.profilecars

import androidx.lifecycle.*
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.CarsRepository
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import com.catasoft.autoclub.util.getAvatarDownloadUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel

@Inject
constructor(
    private val carsRepository: ICarsRepository
) : ViewModel() {

    var carsList: LiveData<List<CarProfileModel>> = MutableLiveData()

    fun getUserCarsList(uid: String){
        viewModelScope.launch {
            carsList = liveData {
                emit(carsRepository.getCarsByUserId(uid).map {
                    return@map CarProfileModel(
                        id=it.id,
                        make = it.make,
                        model = it.model,
                        photoDownloadLink = it.getAvatarDownloadUri()
                    )
                })
            }
        }
    }
}