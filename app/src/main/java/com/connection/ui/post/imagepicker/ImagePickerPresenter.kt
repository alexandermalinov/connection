package com.connection.ui.post.imagepicker

import com.connection.ui.gallery.GalleryPresenter

interface ImagePickerPresenter : GalleryPresenter {

    fun onNextClick()

    fun onDiscardClick()

    fun onChangeClick()

    fun onSettingsClick()
}