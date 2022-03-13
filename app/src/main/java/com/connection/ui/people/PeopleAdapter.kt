package com.connection.ui.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemUserBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.people.PeopleListItemUiModel

class PeopleAdapter : DataBoundListAdapter<PeopleListItemUiModel, ListItemUserBinding>(
    object : DiffUtil.ItemCallback<PeopleListItemUiModel>() {

        override fun areItemsTheSame(
            oldItem: PeopleListItemUiModel,
            newItem: PeopleListItemUiModel
        ) = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: PeopleListItemUiModel,
            newItem: PeopleListItemUiModel
        ) = oldItem == newItem
    }
) {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemUserBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_chat_user,
            parent,
            false
        )

    override fun bind(
        binding: ListItemUserBinding,
        item: PeopleListItemUiModel
    ) {
        binding.model = item
        //binding.presenter = bindingPresenter
    }
}