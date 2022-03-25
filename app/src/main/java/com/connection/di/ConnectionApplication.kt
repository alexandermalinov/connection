package com.connection.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.connection.utils.common.Constants.GET_STREAM_API_KEY
import com.sendbird.android.SendBird
import com.sendbird.android.SendBirdException
import com.sendbird.android.handlers.InitResultHandler
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.livedata.ChatDomain
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class ConnectionApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration
        .Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        ChatClient
            .Builder(GET_STREAM_API_KEY, this)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}