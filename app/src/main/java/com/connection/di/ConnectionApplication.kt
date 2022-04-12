package com.connection.di

import android.app.Application
import android.provider.UserDictionary.Words.APP_ID
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.connection.utils.common.Constants.GET_STREAM_API_KEY
import com.connection.utils.common.Constants.SENDBIRD_APP_ID
import com.sendbird.android.SendBird
import com.sendbird.android.SendBirdException
import com.sendbird.android.handlers.InitResultHandler
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
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
        SendBird.init(SENDBIRD_APP_ID, applicationContext, false, object : InitResultHandler {
            override fun onMigrationStarted() {
                // This won't be called if useLocalCaching is set to false.
            }

            override fun onInitFailed(e: SendBirdException) {
                // This won't be called if useLocalCaching is set to false.
            }

            override fun onInitSucceed() {
                Timber.i("Application", "Called when initialization is completed.")
            }
        })
    }
}