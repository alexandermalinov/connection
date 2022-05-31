package com.connection.ui.people.notconnected

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemConnectedUserBinding
import com.connection.databinding.ListItemInvitationBinding
import com.connection.databinding.ListItemNotConnectedUserBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.people.connected.ConnectedPeopleListItemUiModel
import com.connection.vo.people.invitations.InvitationListItemUiModel
import com.connection.vo.people.notconnected.NotConnectedPeopleListItemUiModel

class NotConnectedAdapter(val presenter: NotConnectedPresenter) :
    DataBoundListAdapter<NotConnectedPeopleListItemUiModel, ListItemNotConnectedUserBinding>(
        object : DiffUtil.ItemCallback<NotConnectedPeopleListItemUiModel>() {

            override fun areItemsTheSame(
                oldItem: NotConnectedPeopleListItemUiModel,
                newItem: NotConnectedPeopleListItemUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: NotConnectedPeopleListItemUiModel,
                newItem: NotConnectedPeopleListItemUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemNotConnectedUserBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_not_connected_user,
            parent,
            false
        )

    override fun bind(
        binding: ListItemNotConnectedUserBinding,
        item: NotConnectedPeopleListItemUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}