package com.connection.utils.image

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.connection.navigation.External
import com.connection.utils.common.Constants.IMAGE_TYPE
import com.connection.utils.common.Constants.SELECT_IMAGE_KEY

interface ActivityResultHandler {

    fun provideObserver(destination: External): ActivityResultObserver
}

interface ActivityResultObserver {

    fun launch()
}

class SelectImageObserver(
    private val registry: ActivityResultRegistry,
    private val onSelect: (Uri?) -> Unit
) : DefaultLifecycleObserver, ActivityResultObserver {

    private lateinit var selectImageFromGalleryResult: ActivityResultLauncher<Array<String>>

    override fun onCreate(owner: LifecycleOwner) {
        selectImageFromGalleryResult = registry.register(
            SELECT_IMAGE_KEY,
            owner,
            GetImage()
        ) { uri ->
            onSelect.invoke(uri)
        }
    }

    override fun launch() {
        selectImageFromGalleryResult.launch(arrayOf(IMAGE_TYPE))
    }
}