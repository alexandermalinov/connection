package com.connection.ui.connectiontab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemChatUserBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.connectiontab.ConnectionChatUiModel

class ConnectionsAdapter : DataBoundListAdapter<ConnectionChatUiModel, ListItemChatUserBinding>(
    object : DiffUtil.ItemCallback<ConnectionChatUiModel>() {

        override fun areItemsTheSame(
            oldItem: ConnectionChatUiModel,
            newItem: ConnectionChatUiModel
        ) = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: ConnectionChatUiModel,
            newItem: ConnectionChatUiModel
        ) = oldItem == newItem
    }
) {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemChatUserBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_chat_user,
            parent,
            false
        )

    override fun bind(
        binding: ListItemChatUserBinding,
        item: ConnectionChatUiModel
    ) {
        binding.model = item
        //binding.presenter = bindingPresenter
    }
}