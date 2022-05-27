package com.connection.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.PICTURE
import com.connection.utils.common.Constants.USERNAME
import com.connection.utils.common.Constants.USER_PICTURE
import com.connection.vo.image.ImageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), ImagePresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ImageUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(obtainUiData())

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun obtainUiData() = with(savedStateHandle) {
        ImageUiModel(
            url = get(PICTURE) ?: EMPTY,
            senderImage = get(USER_PICTURE) ?: EMPTY,
            senderName = get(USERNAME) ?: EMPTY
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }
}