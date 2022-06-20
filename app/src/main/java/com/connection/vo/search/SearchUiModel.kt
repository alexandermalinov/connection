package com.connection.vo.search

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.R
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.INVALID_RES
import com.connection.utils.common.TextRes

data class SearchUiModel(
    val searchList: List<SearchListItemUiModel> = emptyList(),
    var emptySearchResultState: Boolean = false,
    var recentSearchesSate: Boolean = true,
    val errorTextRes: Int = INVALID_RES
) : BaseObservable() {

    @get:Bindable
    var resultsCount: TextRes = TextRes(searchList.size.toString(), R.string.results_count)
        set(value) {
            field = value
            emptySearchResultState = searchList.isEmpty()
            notifyPropertyChanged(BR.resultsCount)
        }

    @get:Bindable
    var loadingState: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingState)
        }
}
