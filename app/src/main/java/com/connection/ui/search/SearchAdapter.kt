package com.connection.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemSearchBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.search.SearchListItemUiModel

class SearchAdapter() :
    DataBoundListAdapter<SearchListItemUiModel, ListItemSearchBinding>(
        object : DiffUtil.ItemCallback<SearchListItemUiModel>() {

            override fun areItemsTheSame(
                oldItem: SearchListItemUiModel,
                newItem: SearchListItemUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: SearchListItemUiModel,
                newItem: SearchListItemUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemSearchBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_search,
            parent,
            false
        )

    override fun bind(
        binding: ListItemSearchBinding,
        item: SearchListItemUiModel
    ) {
        binding.model = item
        //binding.presenter = presenter
    }
}