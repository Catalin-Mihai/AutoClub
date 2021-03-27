package com.catasoft.autoclub.ui.main.addcar

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.model.car.CarCreationModel
import com.catasoft.autoclub.model.car.toCarModel
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.State
import com.catasoft.autoclub.repository.remote.ICarsRepository
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
class AddCarViewModel

@Inject
constructor(
    private val mCarsRepository: ICarsRepository,
    private val mUsersRepository: IUsersRepository
): ViewModel() {

    //Car info live data
    val carModelState: MutableLiveData<CarModelState> = MutableLiveData()
    val carMakeState: MutableLiveData<CarMakeState> = MutableLiveData()
    val carYearState: MutableLiveData<CarYearState> = MutableLiveData()
    val photoState: MutableLiveData<PhotoState> = MutableLiveData()

    //Number plate live data
    val numberPlateAvailable: MutableLiveData<NumberPlateState> = MutableLiveData()
    private val checkChannel = Channel<String>()

    //Page input verdicts as live data
    //Useful for page navigation checks
    var currentPageValidState: MutableLiveData<Boolean> = MutableLiveData()
//    var pageCarNumberPlateValidState = MutableLiveData(false)

    //Useful to know when the car has been saved
    var carSavedState: MutableLiveData<Boolean> = MutableLiveData()

    //The car model that will be build based on the inputs
    val carEntity: CarCreationModel = CarCreationModel()

    @BindingAdapter("bind:imageBitmap")
    fun loadImage(iv: ImageView, bitmap: Bitmap?) {
        iv.setImageBitmap(bitmap)
    }


    init {
        // Listen for channel values.
        // Check every last request that is sent from the View (with respect to the debounce time)
        viewModelScope.launch {
            checkChannel.consumeAsFlow()
                .debounce(500) //Do not process faster than 500 ms between requests
                .filter { it.isNotBlank() }
                .collect {
                    Timber.e("Channel: %s", it)
                    checkAndSaveNumberPlate(it)
                }
        }
    }

    private fun checkIfExistsCarInfoInputs() {
        if(carModelState.value == null)
            carModelState.postValue(CarModelState.Null)

        if(carModelState.value == null)
            carMakeState.postValue(CarMakeState.Null)

        if(carYearState.value == null)
            carYearState.postValue(CarYearState.Null)

        if(photoState.value == null)
            photoState.postValue(PhotoState.Null)
    }

    fun validatePhoto(newPhoto: Bitmap?){
        if(newPhoto == null)
            photoState.postValue(PhotoState.Null)
        else
            photoState.postValue(PhotoState.Valid(newPhoto))
    }

    fun validateNewCarModel(newCarModel: String?){
        if(newCarModel.isNullOrBlank())
            carModelState.postValue(CarModelState.Null)
        else
            carModelState.postValue(CarModelState.Valid(newCarModel))
    }

    fun validateNewCarMake(newCarMake: String?){
        if(newCarMake.isNullOrBlank())
            carMakeState.postValue(CarMakeState.Null)
        else
            carMakeState.postValue(CarMakeState.Valid(newCarMake))
    }

    fun validateNewCarYear(newCarYear: String?){
        if(newCarYear == null) {
            carYearState.postValue(CarYearState.Null)
            return
        }

        kotlin.runCatching { Integer.parseInt(newCarYear) }
            .onSuccess {
                if(isValidYear(it))
                    carYearState.postValue(CarYearState.Valid(it))
                else
                    carYearState.postValue(CarYearState.NotInInterval(MIN_YEAR, MAX_YEAR))
            }
            .onFailure { carYearState.postValue(CarYearState.Null) }
    }

    fun getYearInterval(): Pair<Int, Int> = Pair(MIN_YEAR, MAX_YEAR)

    private fun isValidYear(year: Int): Boolean {
        return year in MIN_YEAR..MAX_YEAR
    }

    fun validatePage(pageNumber: Int){
        when(pageNumber){
            0 -> checkAndSaveCarInfo()
            1 -> {
                if (numberPlateAvailable.value != NumberPlateState.NotUsed) {
                    numberPlateAvailable.postValue(NumberPlateState.InvalidFormat)
                    currentPageValidState.postValue(false)
                } else {
                    currentPageValidState.postValue(true)
                }
            }
        }
    }

/*    fun isPageValid(pageNumber: Int): Boolean{
        when(pageNumber){
            0 -> return pageCarInfoValid
            1 -> return pageCarNumberPlateValid
        }
        return false
    }*/

    private fun checkAndSaveCarInfo() {

        var carMake: String
        var carModel: String
        var carYear: Int
        var photo: Bitmap

        checkIfExistsCarInfoInputs()

        //If the cast works, then the input is good
        kotlin.runCatching {
            carMake = (carMakeState.value as CarMakeState.Valid).carMake
            carModel = (carModelState.value as CarModelState.Valid).carModel
            carYear = (carYearState.value as CarYearState.Valid).carYear
            photo = (photoState.value as PhotoState.Valid).photo


            carEntity.model = carModel
            carEntity.make = carMake
            carEntity.year = carYear
            carEntity.photo = photo

            currentPageValidState.postValue(true)

        }.onFailure {
            currentPageValidState.postValue(false)
        }
    }


    // Car Number methods

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

    private suspend fun checkAndSaveNumberPlate(numberPlate: String)
    {
        //We have a valid user register model. (validated throughout the register steps)
        //So, no more validations

        if(!isValidNumberPlateFormat(numberPlate)){
            numberPlateAvailable.postValue(NumberPlateState.InvalidFormat)
            return
        }

        BaseRepository.singleResultAsStateFlow {
            mUsersRepository.getUserByNumberPlate(numberPlate)
        }.collect { state ->
            when(state){
                is State.Loading -> {
                    numberPlateAvailable.postValue(NumberPlateState.Fetching)
                }
                is State.Success -> {
                    if (state.data == null) {
                        numberPlateAvailable.postValue(NumberPlateState.NotUsed)
                        carEntity.numberPlate = numberPlate
                    } else
                        numberPlateAvailable.postValue(NumberPlateState.Used(state.data))
                }
                is State.Failed -> {
                    numberPlateAvailable.postValue(NumberPlateState.FetchError)
                }
            }
        }
    }


    // Final method to save the all inputs from the all pages
    fun saveCarToDatabase() {
        viewModelScope.launch {
            kotlin.runCatching {
                //Convert the car creation model to the car database model
                var car = carEntity.toCarModel()

                //Put some more info
                car.ownerUid = CurrentUser.getUid()

                //Get the saved version (should have id field filled)
                car = mCarsRepository.addCar(car)

                //If the new car has been created, the id field will be populated.
                car.id?.let { carId ->
                    carEntity.photo?.let { photo ->
                        mCarsRepository.setAvatar(carId, photo)
                    }
                }

            }.onSuccess {
                carSavedState.postValue(true)
            }.onFailure {
                carSavedState.postValue(false)
            }
        }
    }

    /*fun updatePhoto(photo: Bitmap){
        viewModelScope.launch {
            kotlin.runCatching {
                mUsersRepository.setAvatar(CurrentUser.getUid()!!, photo)
            }.onFailure {
                Timber.e("Nu putem actualiza avatarul!")
            }
        }
    }

    fun updateCarModel(displayName: String?) {

        val userCopy = CurrentUser.getEntity()
        userCopy.name = displayName

        viewModelScope.launch {
            mUsersRepository.updateByMerging(userCopy)
        }
    }

    fun updateCarMake(facebookProfile: String?){

        val userCopy = CurrentUser.getEntity()
        userCopy.facebookProfile = facebookProfile

        viewModelScope.launch {
            mUsersRepository.updateByMerging(userCopy)
        }
    }*/


    companion object {

        const val MIN_YEAR = 1900
        const val MAX_YEAR = 2021

        sealed class NumberPlateState {
            data class Used(val ownedByUser: User) : NumberPlateState()
            object NotUsed : NumberPlateState()
            object Fetching: NumberPlateState()
            object FetchError: NumberPlateState()
            object InvalidFormat: NumberPlateState()
        }

        sealed class CarModelState {
            object Null : CarModelState()
            data class Valid(val carModel: String) : CarModelState()
        }

        sealed class CarMakeState {
            object Null: CarMakeState()
            data class Valid(val carMake: String): CarMakeState()
        }

        sealed class CarYearState {
            object Null: CarYearState()
            data class NotInInterval(val start: Int, val end: Int): CarYearState()
            data class Valid(val carYear: Int): CarYearState()
        }

        sealed class PhotoState {
            object Null: PhotoState()
            data class Valid(val photo: Bitmap): PhotoState()
        }
    }


}