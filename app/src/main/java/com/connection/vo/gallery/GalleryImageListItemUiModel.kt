package com.connection.vo.gallery

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import java.util.*

data class GalleryImageListItemUiModel(
    val id: UUID,
    val image: String,
) : BaseObservable() {

    @get:Bindable
    var isSelected: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
}

fun List<Uri>.toUiModel() = map { image ->
    GalleryImageListItemUiModel(
        id = UUID.randomUUID(),
        image = image.toString()
    )
}