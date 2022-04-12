package com.connection.ui.people.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemUserBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.people.PeopleListItemUiModel

class PeopleAdapter(val presenter: PeoplesPresenter) :
    DataBoundListAdapter<PeopleListItemUiModel, ListItemUserBinding>(
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

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemUserBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user,
            parent,
            false
        )

    override fun bind(
        binding: ListItemUserBinding,
        item: PeopleListItemUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}