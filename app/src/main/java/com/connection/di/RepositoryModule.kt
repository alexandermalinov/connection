package com.connection.di

import android.app.Application
import android.content.Context
import com.connection.data.repository.user.UserRemoteSource
import com.connection.data.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRemoteSource(auth: FirebaseAuth): UserRepository.RemoteSource =
        UserRemoteSource(auth)

    /*
    TODO Add network checking
    @Singleton
    @Provides
    fun provideNetworkChecker(application: Application): Context = NetworkChecker(application)*/
}