package com.connection.ui.people.invitation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemInvitationBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.people.invitations.InvitationListItemUiModel

class InvitationAdapter(val presenter: InvitationsPresenter) :
    DataBoundListAdapter<InvitationListItemUiModel, ListItemInvitationBinding>(
        object : DiffUtil.ItemCallback<InvitationListItemUiModel>() {

            override fun areItemsTheSame(
                oldItem: InvitationListItemUiModel,
                newItem: InvitationListItemUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: InvitationListItemUiModel,
                newItem: InvitationListItemUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemInvitationBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_invitation,
            parent,
            false
        )

    override fun bind(
        binding: ListItemInvitationBinding,
        item: InvitationListItemUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}