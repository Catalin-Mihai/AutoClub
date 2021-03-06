package com.catasoft.autoclub.ui.main.profileedit

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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

    fun validateFields(newDisplayName: String?, newFacebookProfile: String?, newPhoto: Bitmap?) {
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

    }

    fun updatePhoto(photo: Bitmap){
        viewModelScope.launch {
            kotlin.runCatching {
                mUsersRepository.setAvatar(CurrentUser.getUid()!!, photo)
            }.onFailure {
                Timber.e("Nu putem actualiza avatarul!")
            }
        }
    }

    fun updateDisplayName(displayName: String?) {

        val userCopy = CurrentUser.getEntity()
        userCopy.name = displayName

        viewModelScope.launch {
            mUsersRepository.updateByMerging(userCopy)
        }
    }

    fun updateFacebookProfile(facebookProfile: String?){

        val userCopy = CurrentUser.getEntity()
        userCopy.facebookProfile = facebookProfile

        viewModelScope.launch {
            mUsersRepository.updateByMerging(userCopy)
        }
    }

    companion object {
        sealed class DisplayNameState {
            object Used : DisplayNameState()
            object Null : DisplayNameState()
            object Unchanged : DisplayNameState()
            data class Valid(val displayName: String) : DisplayNameState()
        }

        sealed class FacebookProfileState {
            object Used : FacebookProfileState()
            object Null: FacebookProfileState()
            object Unchanged: FacebookProfileState()
            data class Valid(val facebookProfile: String): FacebookProfileState()
        }

        sealed class PhotoState {
            object Error: PhotoState()
            data class Valid(val photo: Bitmap): PhotoState()
        }
    }


}