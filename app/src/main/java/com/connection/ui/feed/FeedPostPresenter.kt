package com.connection.ui.feed

import com.connection.vo.post.PostUiModel

interface FeedPostPresenter {

    fun onLikeClick(id: String)

    fun onCommentsClick(id: String)

    fun onSaveClick(id: String)

    fun onConnectClick(post: PostUiModel)
}