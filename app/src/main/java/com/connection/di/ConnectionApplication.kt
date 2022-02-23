package com.connection.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.connection.utils.common.Constants.COMET_CHAT_APP_ID
import com.connection.utils.common.Constants.COMET_CHAT_REGION
import dagger.hilt.android.HiltAndroidApp
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

    private val appSettings = AppSettings
        .AppSettingsBuilder()
        .subscribePresenceForAllUsers()
        .setRegion(COMET_CHAT_REGION)
        .build()

    override fun onCreate() {
        super.onCreate()
        CometChat.init(
            this,
            COMET_CHAT_APP_ID,
            appSettings,
            object : CometChat.CallbackListener<String>() {
                override fun onSuccess(successMessage: String) {
                    Timber.i("Initialization completed successfully")
                }

                override fun onError(e: CometChatException) {
                    Timber.i("Initialization failed with exception: $e.message")
                }
            })
    }
}