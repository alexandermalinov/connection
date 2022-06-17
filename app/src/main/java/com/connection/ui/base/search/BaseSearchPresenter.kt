package com.connection.ui.base.search

import kotlinx.coroutines.flow.Flow

interface BaseSearchPresenter {

    fun onSearchChanged(searchFlow: Flow<CharSequence>)
}