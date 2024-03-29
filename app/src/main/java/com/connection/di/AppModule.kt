package com.connection.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseStorage() = Firebase.storage

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance(
        "https://connection-18947-default-rtdb.europe-west1.firebasedatabase.app"
    )
}