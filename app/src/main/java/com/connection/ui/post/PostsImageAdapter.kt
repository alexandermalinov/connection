package com.connection.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemPostImageBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.post.PostUiModel

class PostsImageAdapter(val presenter: PostsPresenter) :
    DataBoundListAdapter<PostUiModel, ListItemPostImageBinding>(
        object : DiffUtil.ItemCallback<PostUiModel>() {

            override fun areItemsTheSame(
                oldItem: PostUiModel,
                newItem: PostUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: PostUiModel,
                newItem: PostUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemPostImageBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_post_image,
            parent,
            false
        )

    override fun bind(
        binding: ListItemPostImageBinding,
        item: PostUiModel
    ) {
        binding.model = item
        binding.presenter = presenter
    }
}