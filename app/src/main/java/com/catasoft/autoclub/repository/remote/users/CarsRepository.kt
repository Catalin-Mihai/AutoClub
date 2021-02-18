package com.catasoft.autoclub.repository.remote.users

import com.catasoft.autoclub.model.User
import com.catasoft.autoclub.repository.BaseRepository
import com.catasoft.autoclub.repository.Constants
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ICarsRepository{
}

class CarsRepository @Inject constructor(): ICarsRepository, BaseRepository(){

}