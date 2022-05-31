package com.connection.vo.post.imagepicker

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.gallery.GalleryImageListItemUiModel

data class ImagePickerUiModel(
    val selectedPicture: String = EMPTY,
    val galleryPictures: List<GalleryImageListItemUiModel> = emptyList()
)