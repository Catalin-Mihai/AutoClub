package com.catasoft.autoclub.ui.main.profileedit

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProfileEditViewModel

@Inject
constructor(
    private val mUsersRepository: IUsersRepository
): ViewModel() {

    val displayNameState: MutableLiveData<DisplayNameState> = MutableLiveData()
    val facebookProfileState: MutableLiveData<FacebookProfileState> = MutableLiveData()
    val photoState: MutableLiveData<PhotoState> = MutableLiveData()

    /*fun validateFields(newDisplayName: String?, newFacebookProfile: String?, newPhoto: Bitmap?) {
        //TODO: Check if the values are already in use (or not)
        //For now return true as it's ok to have anything

        if(newDisplayName.isNullOrBlank())
            displayNameState.postValue(DisplayNameState.Null)
        else {
            when (newDisplayName) {
                CurrentUser.getEntity().name -> displayNameState.postValue(DisplayNameState.Unchanged)
                else -> displayNameState.postValue(DisplayNameState.Valid(newDisplayName))
            }
        }


        if(newFacebookProfile.isNullOrBlank())
            facebookProfileState.postValue(FacebookProfileState.Null)
        else{
            when (newFacebookProfile) {
                CurrentUser.getEntity().facebookProfile -> facebookProfileState.postValue(FacebookProfileState.Unchanged)
                else -> facebookProfileState.postValue(FacebookProfileState.Valid(newFacebookProfile))
            }
        }


        if(newPhoto == null)
            photoState.postValue(PhotoState.Error)
        else
            photoState.postValue(PhotoState.Valid(newPhoto))

    }*/

    fun updatePhoto(photo: Bitmap?){

        if(photo == null) {
            photoState.postValue(PhotoState.Error)
            return
        }

        viewModelScope.launch {
            kotlin.runCatching {
                mUsersRepository.setAvatar(CurrentUser.getUid()!!, photo)
            }.onFailure {
                Timber.e("Nu putem actualiza avatarul!")
                photoState.postValue(PhotoState.Error)
            }.onSuccess {
                photoState.postValue(PhotoState.Saved(photo))
            }
        }
    }

    fun updateDisplayName(displayName: String?) {

        if(displayName.isNullOrBlank()) {
            displayNameState.postValue(DisplayNameState.Null)
            return
        }

       if(displayName == CurrentUser.getEntity().name) {
            displayNameState.postValue(DisplayNameState.Unchanged)
            return
        }

        val userCopy = CurrentUser.getEntity()
        userCopy.name = displayName
        userCopy.normalizedName = userCopy.name?.toUpperCase(Locale.getDefault())

        viewModelScope.launch {
            kotlin.runCatching {
                mUsersRepository.updateByMerging(userCopy)
            }.onFailure {
                displayNameState.postValue(DisplayNameState.Error)
            }.onSuccess {
                displayNameState.postValue(DisplayNameState.Saved(displayName))
            }
        }
    }

    fun updateFacebookProfile(facebookProfile: String?){

        if(facebookProfile.isNullOrBlank()) {
            facebookProfileState.postValue(FacebookProfileState.Null)
            return
        }

        if(facebookProfile == CurrentUser.getEntity().facebookProfile ) {
            facebookProfileState.postValue(FacebookProfileState.Unchanged)
            return
        }

        val userCopy = CurrentUser.getEntity()
        userCopy.facebookProfile = facebookProfile

        viewModelScope.launch {
            kotlin.runCatching {
                mUsersRepository.updateByMerging(userCopy)
            }.onFailure {
                facebookProfileState.postValue(FacebookProfileState.Error)
            }.onSuccess {
                facebookProfileState.postValue(FacebookProfileState.Saved(facebookProfile))
            }
        }
    }

    companion object {
        sealed class DisplayNameState {
            object Used : DisplayNameState()
            object Null : DisplayNameState()
            object Error : DisplayNameState()
            object Unchanged : DisplayNameState()
            data class Saved(val displayName: String) : DisplayNameState()
        }

        sealed class FacebookProfileState {
            object Used : FacebookProfileState()
            object Null: FacebookProfileState()
            object Error: FacebookProfileState()
            object Unchanged: FacebookProfileState()
            data class Saved(val facebookProfile: String): FacebookProfileState()
        }

        sealed class PhotoState {
            object Error: PhotoState()
            data class Saved(val photo: Bitmap): PhotoState()
        }
    }


}