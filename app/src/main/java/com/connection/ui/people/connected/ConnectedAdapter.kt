package com.connection.ui.people.connected

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemConnectedUserBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.people.connected.ConnectedPeopleListItemUiModel

class ConnectedAdapter(val presenter: ConnectedPresenter) :
    DataBoundListAdapter<ConnectedPeopleListItemUiModel, ListItemConnectedUserBinding>(
        object : DiffUtil.ItemCallback<ConnectedPeopleListItemUiModel>() {

            override fun areItemsTheSame(
                oldItem: ConnectedPeopleListItemUiModel,
                newItem: ConnectedPeopleListItemUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: ConnectedPeopleListItemUiModel,
                newItem: ConnectedPeopleListItemUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemConnectedUserBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_connected_user,
            parent,
            false
        )

    override fun bind(
        binding: ListItemConnectedUserBinding,
        item: ConnectedPeopleListItemUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}