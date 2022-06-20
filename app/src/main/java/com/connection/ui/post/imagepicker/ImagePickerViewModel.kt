package com.connection.ui.post.imagepicker

import android.Manifest
import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.connection.R
import com.connection.navigation.NavigationGraph
import com.connection.navigation.SettingsNavigation
import com.connection.ui.base.BaseViewModel
import com.connection.ui.gallery.GalleryLoader
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.PICTURE
import com.connection.utils.livedata.SingleLiveEvent
import com.connection.utils.permissions.*
import com.connection.vo.dialogs.TitleMessageDialog
import com.connection.vo.gallery.GalleryImageListItemUiModel
import com.connection.vo.gallery.toUiModel
import com.connection.vo.post.imagepicker.ImagePickerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val application: Application,
    private val permissionChecker: PermissionChecker
) : BaseViewModel(), ImagePickerPresenter, PermissionStateHandler, LifecycleObserver {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ImagePickerUiModel>
        get() = _uiLiveData

    val galleryLiveData: LiveData<List<GalleryImageListItemUiModel>>
        get() = _galleryLiveData

    val permissionLiveData: LiveData<Permission>
        get() = _permissionLiveData

    private val _uiLiveData = MutableLiveData(ImagePickerUiModel())
    private val _galleryLiveData = MutableLiveData(emptyList<GalleryImageListItemUiModel>())
    private val _permissionLiveData = SingleLiveEvent<Permission>()

    init {
        if (permissionChecker.isPermissionGranted()) {
            _uiLiveData.value?.setLoadingState()
            loadGallery()
        } else
            _uiLiveData.value?.setDeniedState()
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun loadGallery() {
        GalleryLoader(application).loadGallery().toUiModel().let { images ->
            _galleryLiveData.value = images
            _uiLiveData.value?.apply {
                selectedPicture = images.takeIf { it.isNotEmpty() }?.first()?.image ?: EMPTY
                setGrantedState()
            }
        }
    }

    private fun setShowRationaleState() {
        _dialogLiveData.value = TitleMessageDialog(
            message = R.string.grant_permission_gallery,
            positiveButtonClickListener = { requestPermission() },
            negativeButtonClickListener = { _uiLiveData.value?.setInitialState() }
        )
    }

    private fun requestPermission() {
        _permissionLiveData.value =
            PermissionRequest(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun updateUiState() {
        if (permissionChecker.isPermissionGranted().not())
            _uiLiveData.value?.setInitialState()
        else
            requestPermission()
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onImageClick(id: UUID) {
        _galleryLiveData.value
            ?.find { picture -> picture.id == id }
            ?.let { _uiLiveData.value?.selectedPicture = it.image }
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

    override fun onChangeClick() {
        requestPermission()
    }

    override fun onSettingsClick() {
        _navigationLiveData.value = SettingsNavigation
    }

    override fun onPermissionState(state: PermissionState) {
        _uiLiveData.value?.apply {
            when (state) {
                is GrantedState -> loadGallery()
                is DeniedState -> setDeniedState()
                else -> {
                    setInitialState()
                    setShowRationaleState()
                }
            }
        }
    }
}