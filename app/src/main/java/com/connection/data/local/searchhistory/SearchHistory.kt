package com.connection.data.local.searchhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.connection.vo.search.SearchListItemUiModel

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey
    @ColumnInfo(name = "searched_user_id")
    val searchedUserId: String,
    @ColumnInfo(name ="logged_user_id")
    val loggedUserId: String,
    @ColumnInfo(name = "searched_username")
    val searchedUsername: String,
    @ColumnInfo(name = "searched_profile_picture")
    val searchedProfilePicture: String
)

fun List<SearchHistory>.toUiModels() = map { it.toUiModel() }

private fun SearchHistory.toUiModel() = SearchListItemUiModel(
    userId = searchedUserId,
    username = searchedUsername,
    userPicture = searchedProfilePicture
)