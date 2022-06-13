package com.connection.vo.search

import com.connection.data.api.local.searchhistory.SearchHistory
import com.connection.data.api.remote.model.user.UserData
import com.connection.utils.common.Constants.EMPTY

data class SearchListItemUiModel(
    val userId: String = EMPTY,
    val username: String = EMPTY,
    val userPicture: String = EMPTY
)

fun SearchListItemUiModel.toModel(loggedUserId: String) = SearchHistory(
    loggedUserId = loggedUserId,
    searchedUserId = userId,
    searchedUsername = username,
    searchedProfilePicture = userPicture
)

fun List<UserData>.toUiModel() = map { user -> user.toUiModel() }

private fun UserData.toUiModel() = SearchListItemUiModel(
    userId = id,
    username = username,
    userPicture = picture
)
