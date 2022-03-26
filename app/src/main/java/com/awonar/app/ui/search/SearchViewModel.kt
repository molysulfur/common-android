package com.awonar.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.search.SearchRequest
import com.awonar.android.shared.domain.search.ClearRecentlyUseCase
import com.awonar.android.shared.domain.search.GetRecentlySearchUseCase
import com.awonar.android.shared.domain.search.GetSearchUseCase
import com.awonar.app.ui.search.adapter.SearchItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchUseCase: GetSearchUseCase,
    private val getRecentlySearchUseCase: GetRecentlySearchUseCase,
    private val clearRecentlyUseCase: ClearRecentlyUseCase,
) : ViewModel() {

    private val _filterState = MutableStateFlow("all")
    private val _searchText = MutableStateFlow("")

    private val _searchItemList = MutableStateFlow<MutableList<SearchItem>>(mutableListOf())
    val searchItemList get() = _searchItemList

    init {
        val flow = combine(_filterState, _searchText) { filter, search ->
            if (_searchText.value.isNullOrBlank()) {
                getRecentlySearchUseCase(Unit).collect {
                    val data = it.successOr(emptyList())
                    val itemList = mutableListOf<SearchItem>()
                    itemList.add(SearchItem.SectorItem("Recent", "Clear"))
                    data.filter { result ->
                        if (filter == "all") {
                            true
                        } else {
                            result.type == filter
                        }
                    }.forEach { result ->
                        itemList.add(SearchItem.ListItem(result.data, result.type == "markets"))
                    }
                    _searchItemList.value = itemList
                }
            } else {
                getSearchUseCase(SearchRequest(
                    keyword = _searchText.value,
                    1,
                    _filterState.value
                )).collect {
//                    val data = it.successOr(null)
//                    val itemList = mutableListOf<SearchItem>()
//                    data?.markets?.forEach { result ->
//                        itemList.add(SearchItem.ListItem())
//                    }
                }
            }
            return@combine true
        }

        viewModelScope.launch {
            flow.collect {
            }
        }

    }

    fun filter(filter: String) {
        _filterState.value = filter
    }

    fun search(keyword: String) {
        _searchText.value = keyword
    }

    fun clearRecently() {
        clearRecentlyUseCase(Unit)
        _searchItemList.value = mutableListOf(SearchItem.SectorItem("Recent", "Clear"))
    }

}