package com.connection.ui.feed

interface FeedPostPresenter {

    fun onLikeClick(id: String)

    fun onCommentsClick(id: String)

    fun onSaveClick(id: String)
}