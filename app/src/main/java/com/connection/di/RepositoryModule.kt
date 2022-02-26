package com.connection.di

import android.app.Application
import android.content.Context
import com.connection.data.repository.user.UserRemoteSource
import com.connection.data.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideFirebaseAuth(auth: FirebaseAuth): FirebaseAuth = auth

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