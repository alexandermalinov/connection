package com.connection.di

import com.connection.data.local.searchhistory.SearchHistoryDao
import com.connection.data.repository.chatmessage.ChatMessageRemoteSource
import com.connection.data.repository.chatmessage.ChatMessageRepository
import com.connection.data.repository.chattab.ChatTabRemoteSource
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.post.PostRemoteSource
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.search.SearchLocalSource
import com.connection.data.repository.search.SearchRemoteSource
import com.connection.data.repository.search.SearchRepository
import com.connection.data.repository.user.UserRemoteSource
import com.connection.data.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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
    fun provideUserRemoteSource(
        auth: FirebaseAuth,
        db: FirebaseDatabase,
        storage: FirebaseStorage
    ): UserRepository.RemoteSource = UserRemoteSource(auth, db, storage)

    @Singleton
    @Provides
    fun provideChatTabRemoteSource(): ChatTabRepository.RemoteSource = ChatTabRemoteSource()

    @Singleton
    @Provides
    fun provideChatMessagesRemoteSource(): ChatMessageRepository.RemoteSource =
        ChatMessageRemoteSource()

    @Singleton
    @Provides
    fun providePostsRemoteSource(
        auth: FirebaseAuth,
        db: FirebaseDatabase,
        storage: FirebaseStorage
    ): PostRepository.RemoteSource = PostRemoteSource(auth, db, storage)

    @Singleton
    @Provides
    fun provideSearchLocalSource(searchHistoryDao: SearchHistoryDao): SearchRepository.LocalSource =
        SearchLocalSource(searchHistoryDao)

    @Singleton
    @Provides
    fun provideSearchRemoteSource(db: FirebaseDatabase): SearchRepository.RemoteSource =
        SearchRemoteSource(db)
}