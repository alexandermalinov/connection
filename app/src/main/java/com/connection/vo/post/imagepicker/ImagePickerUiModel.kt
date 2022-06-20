package com.connection.vo.post.imagepicker

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.gallery.GalleryImageListItemUiModel

class ImagePickerUiModel : BaseObservable() {

    @get:Bindable
    var selectedPicture: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedPicture)
        }

    @get:Bindable
    var isInitialState: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.initialState)
        }

    @get:Bindable
    var isLoadingState: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingState)
        }

    @get:Bindable
    var isGrantedState: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.grantedState)
        }

    @get:Bindable
    var isDeniedState: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.deniedState)
        }

    fun setInitialState() {
        isInitialState = true
        isLoadingState = false
        isGrantedState = false
        isDeniedState = false
    }

    fun setLoadingState() {
        isInitialState = false
        isLoadingState = true
        isGrantedState = false
        isDeniedState = false
    }

    fun setGrantedState() {
        isInitialState = false
        isLoadingState = false
        isGrantedState = true
        isDeniedState = false
    }

    fun setDeniedState() {
        isInitialState = false
        isLoadingState = false
        isGrantedState = false
        isDeniedState = true
    }
}
