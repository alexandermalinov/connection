package com.connection.ui.gallery

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContentResolverCompat
import dagger.hilt.android.qualifiers.ApplicationContext

class GalleryLoader(private val application: Application) {

    private val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
    private val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media._ID
    )

    fun loadGallery(): List<Uri> = application.contentResolver
        .query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder,
            null
        )
        ?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val listOfAllImages = mutableListOf<Uri>()
            while (cursor.moveToNext()) {
                listOfAllImages.add(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(idColumn)
                    )
                )
            }
            listOfAllImages.toList()
        }
        ?: emptyList()
}