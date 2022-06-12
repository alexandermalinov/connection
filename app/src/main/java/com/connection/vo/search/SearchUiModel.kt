package com.connection.vo.search

data class SearchUiModel(
    val searchList: List<SearchListItemUiModel> = emptyList(),
    val emptySearchResultState: Boolean = false
)
