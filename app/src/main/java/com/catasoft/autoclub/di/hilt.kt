package com.catasoft.autoclub.di

import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.ICurrentUser
import com.catasoft.autoclub.repository.remote.CarsRepository
import com.catasoft.autoclub.repository.remote.ICarsRepository
import com.catasoft.autoclub.repository.remote.IUsersRepository
import com.catasoft.autoclub.repository.remote.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUsersRepository(repo: UsersRepository): IUsersRepository

    @Singleton
    @Binds
    abstract fun bindCarsRepository(repo: CarsRepository): ICarsRepository
}
