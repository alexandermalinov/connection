package com.connection.ui.post.imagepicker

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.connection.R
import com.connection.navigation.NavigationGraph
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.ui.gallery.GalleryLoader
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.PICTURE
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.gallery.toUiModel
import com.connection.vo.post.imagepicker.ImagePickerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel(), ImagePickerPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ImagePickerUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ImagePickerUiModel())

    init {
        loadGallery()
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun loadGallery() {
        _uiLiveData.value = _uiLiveData.value?.copy(
            galleryPictures = GalleryLoader(application).loadGallery().toUiModel()
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onImageClick(id: UUID) {
        _uiLiveData.value?.galleryPictures
            ?.find { picture -> picture.id == id }
            ?.let { _uiLiveData.value = _uiLiveData.value?.copy(selectedPicture = it.image) }
    }

    override fun onNextClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_imagePickerFragment_to_createPostFragment,
            bundleOf(PICTURE to _uiLiveData.value?.selectedPicture)
        )
    }

    override fun onDiscardClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_imagePickerFragment_to_feedFragment)
    }
}