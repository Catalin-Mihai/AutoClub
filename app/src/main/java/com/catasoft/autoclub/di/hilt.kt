package com.catasoft.autoclub.di

import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import com.catasoft.autoclub.repository.remote.users.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUsersRepository(repo: UsersRepository): IUsersRepository

}
