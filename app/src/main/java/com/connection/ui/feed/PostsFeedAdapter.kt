package com.connection.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemFeedPostBinding
import com.connection.databinding.ListItemPostImageBinding
import com.connection.ui.profile.posts.PostsPresenter
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.post.PostUiModel

class PostsFeedAdapter(val presenter: PostsPresenter) :
    DataBoundListAdapter<PostUiModel, ListItemFeedPostBinding>(
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
    ): ListItemFeedPostBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_feed_post,
            parent,
            false
        )

    override fun bind(
        binding: ListItemFeedPostBinding,
        item: PostUiModel
    ) {
        binding.model = item
        //binding.presenter = presenter
    }
}