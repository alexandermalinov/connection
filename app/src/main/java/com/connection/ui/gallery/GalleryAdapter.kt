package com.connection.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemConnectedUserBinding
import com.connection.databinding.ListItemGalleryImageBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.gallery.GalleryImageListItemUiModel
import com.connection.vo.people.connected.ConnectedPeopleListItemUiModel

class GalleryAdapter(val presenter: GalleryPresenter) :
    DataBoundListAdapter<GalleryImageListItemUiModel, ListItemGalleryImageBinding>(
        object : DiffUtil.ItemCallback<GalleryImageListItemUiModel>() {

            override fun areItemsTheSame(
                oldItem: GalleryImageListItemUiModel,
                newItem: GalleryImageListItemUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: GalleryImageListItemUiModel,
                newItem: GalleryImageListItemUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemGalleryImageBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_gallery_image,
            parent,
            false
        )

    override fun bind(
        binding: ListItemGalleryImageBinding,
        item: GalleryImageListItemUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}