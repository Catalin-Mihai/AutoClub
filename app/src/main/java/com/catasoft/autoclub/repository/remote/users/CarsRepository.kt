package com.catasoft.autoclub.repository.remote.users

import com.catasoft.autoclub.repository.BaseRepository
import javax.inject.Inject

interface ICarsRepository{
}

class CarsRepository @Inject constructor(): ICarsRepository, BaseRepository(){

}