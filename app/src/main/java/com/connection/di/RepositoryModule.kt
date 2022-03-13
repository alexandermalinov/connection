package com.connection.di

import com.connection.data.repository.chat.ChatTabRemoteSource
import com.connection.data.repository.chat.ChatTabRepository
import com.connection.data.repository.user.UserRemoteSource
import com.connection.data.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRemoteSource(
        auth: FirebaseAuth,
        db: FirebaseDatabase,
        storage: FirebaseStorage
    ): UserRepository.RemoteSource = UserRemoteSource(auth, db, storage)

    @Singleton
    @Provides
    fun provideChatTabRemoteSource(
        auth: FirebaseAuth,
        chat: ChatClient
    ): ChatTabRepository.RemoteSource = ChatTabRemoteSource(auth, chat)

    /*
    TODO Add network checking
    @Singleton
    @Provides
    fun provideNetworkChecker(application: Application): Context = NetworkChecker(application)*/
}