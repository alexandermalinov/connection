package com.connection.ui.alltabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemChatFavouriteConnectionBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.alltabs.FavouriteConnectionListItemUiModel

class FavouritesConnectionsAdapter : DataBoundListAdapter<FavouriteConnectionListItemUiModel, ListItemChatFavouriteConnectionBinding>(
    object : DiffUtil.ItemCallback<FavouriteConnectionListItemUiModel>() {

        override fun areItemsTheSame(
            oldItem: FavouriteConnectionListItemUiModel,
            newItem: FavouriteConnectionListItemUiModel
        ) = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: FavouriteConnectionListItemUiModel,
            newItem: FavouriteConnectionListItemUiModel
        ) = oldItem == newItem
    }
) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemChatFavouriteConnectionBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_chat_favourite_connection,
            parent,
            false
        )

    override fun bind(
        binding: ListItemChatFavouriteConnectionBinding,
        item: FavouriteConnectionListItemUiModel
    ) {
        binding.model = item
        //binding.presenter = bindingPresenter
    }
}