package com.awonar.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.shared.domain.feed.GetAllFeedsUseCase
import com.awonar.app.domain.feed.ConvertFeedData
import com.awonar.app.domain.feed.ConvertFeedsToItemsUseCase
import com.awonar.app.ui.feed.adapter.FeedItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getAllFeedsUseCase: GetAllFeedsUseCase,
    private val convertFeedsToItemsUseCase: ConvertFeedsToItemsUseCase,
) : ViewModel() {

    val _pageState = MutableStateFlow(1)
    private val _feedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _feedItems =
        MutableStateFlow<MutableList<FeedItem>>(mutableListOf(FeedItem.LoadingItem()))
    val feedItems get() = _feedItems

    init {
        viewModelScope.launch {
            getFeed()
        }

        viewModelScope.launch {
            _feedsState.collect {
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(it, _pageState.value)).successOr(
                        mutableListOf())
            }
        }
    }

    private suspend fun getFeed() {
        getAllFeedsUseCase(_pageState.value).collectLatest { result ->
            val data = result.successOr(null)
            _pageState.value = data?.page ?: 0
            data?.feeds?.let { newFeeds ->
                val feeds = _feedsState.value.toMutableList()
                feeds.addAll(newFeeds)
                _feedsState.value = feeds
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            getFeed()
        }
    }

    fun refresh() {

    }
}