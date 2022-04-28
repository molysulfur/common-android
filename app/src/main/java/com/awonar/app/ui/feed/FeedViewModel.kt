package com.awonar.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.shared.domain.feed.FeedRequest
import com.awonar.android.shared.domain.feed.GetAllFeedsUseCase
import com.awonar.app.domain.feed.ConvertFeedData
import com.awonar.app.domain.feed.ConvertFeedsToItemsUseCase
import com.awonar.app.ui.feed.adapter.FeedItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getAllFeedsUseCase: GetAllFeedsUseCase,
    private val convertFeedsToItemsUseCase: ConvertFeedsToItemsUseCase,
) : ViewModel() {

    private val _pageState = MutableStateFlow(1)
    private val _feedType = MutableStateFlow("")
    private val _feedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _followFeedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _copyTradeFeedState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _feedItems =
        MutableStateFlow<MutableList<FeedItem>>(mutableListOf(FeedItem.LoadingItem()))
    val feedItems get() = _feedItems

    private val _goToTopState = Channel<Unit>(Channel.CONFLATED)
    val goToTopState get() = _goToTopState.receiveAsFlow()

    init {
        viewModelScope.launch {
            _feedType.collect {
                getFeed()
            }
        }

        viewModelScope.launch {
            _feedsState.collect {
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(it, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }

        viewModelScope.launch {
            _followFeedsState.collect {
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(it, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }

        viewModelScope.launch {
            _copyTradeFeedState.collect {
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(it, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }
    }

    private suspend fun getFeed() {
        getAllFeedsUseCase(FeedRequest(_pageState.value, _feedType.value)).collectLatest { result ->
            val data = result.successOr(null)
            _pageState.value = data?.page ?: 0
            data?.feeds?.let { newFeeds ->
                val feeds = when (_feedType.value) {
                    "following" -> _followFeedsState.value.toMutableList()
                    "copy" -> _copyTradeFeedState.value.toMutableList()
                    else -> _feedsState.value.toMutableList()
                }
                feeds.addAll(newFeeds)
                when (_feedType.value) {
                    "following" -> _followFeedsState.value = feeds
                    "copy" -> _copyTradeFeedState.value = feeds
                    else -> _feedsState.value = feeds
                }

            }
        }
    }

    fun setFeedType(type: String = "") {
        refresh()
        _feedType.value = type
    }

    fun loadMore() {
        viewModelScope.launch {
            getFeed()
        }
    }

    fun refresh() {
        _pageState.value = 1
        _feedsState.value = mutableListOf()
    }

    fun goToTop() {
        viewModelScope.launch {
            _goToTopState.send(Unit)
        }
    }
}