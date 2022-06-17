package com.connection.ui.base.search

import androidx.lifecycle.viewModelScope
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseSearchViewModel : BaseViewModel(), BaseSearchPresenter {

    /* --------------------------------------------------------------------------------------------
     * Protected
    ---------------------------------------------------------------------------------------------*/
    protected var lastSearchText = EMPTY

    fun onSearchChanged(
        searchFlow: Flow<CharSequence>,
        fetchData: suspend (CharSequence) -> Unit
    ) {
        searchFlow
            .debounce(500)
            .onEach { fetchData(it) }
            .launchIn(viewModelScope)
    }
}