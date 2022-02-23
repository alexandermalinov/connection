package com.connection.di

import android.app.Application
import android.content.Context
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

    /*
    TODO Add network checking
    @Singleton
    @Provides
    fun provideNetworkChecker(application: Application): Context = NetworkChecker(application)*/
}