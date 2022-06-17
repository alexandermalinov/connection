package com.connection.ui.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemCommentBinding
import com.connection.databinding.ListItemFeedPostBinding
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.comments.PostCommentListItemUiModel
import com.connection.vo.post.PostUiModel

class CommentsAdapter
    : DataBoundListAdapter<PostCommentListItemUiModel, ListItemCommentBinding>(
    object : DiffUtil.ItemCallback<PostCommentListItemUiModel>() {

        override fun areItemsTheSame(
            oldItem: PostCommentListItemUiModel,
            newItem: PostCommentListItemUiModel
        ) = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: PostCommentListItemUiModel,
            newItem: PostCommentListItemUiModel
        ) = oldItem == newItem
    }
) {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ListItemCommentBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_comment,
            parent,
            false
        )

    override fun bind(
        binding: ListItemCommentBinding,
        item: PostCommentListItemUiModel
    ) {
        binding.model = item
    }
}