package com.catasoft.autoclub.di

import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.repository.ICurrentUser
import com.catasoft.autoclub.repository.remote.users.IUsersRepository
import com.catasoft.autoclub.repository.remote.users.UsersRepository
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

//    @Singleton
//    @Binds
//    abstract fun bindCurrentUser(user: CurrentUser): ICurrentUser
}
