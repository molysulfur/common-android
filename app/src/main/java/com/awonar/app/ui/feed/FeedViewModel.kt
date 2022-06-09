package com.awonar.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedRequest
import com.awonar.android.shared.domain.feed.GetFeedsUseCase
import com.awonar.app.domain.feed.ConvertFeedData
import com.awonar.app.domain.feed.ConvertFeedsToItemsUseCase
import com.awonar.app.ui.feed.adapter.FeedItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedsUseCase: GetFeedsUseCase,
    private val convertFeedsToItemsUseCase: ConvertFeedsToItemsUseCase,
) : ViewModel() {

    private val _pageState = MutableStateFlow(1)
    private val _prefix = MutableStateFlow("")
    private val _feedType = MutableStateFlow<String?>(null)
    private val _instrumentFeeds = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _userFeeds = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _feedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _followFeedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _copyTradeFeedState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    private val _feedItems =
        MutableStateFlow<MutableList<FeedItem>>(mutableListOf())
    val feedItems get() = _feedItems

    private val _goToTopState = Channel<Unit>(Channel.CONFLATED)
    val goToTopState get() = _goToTopState.receiveAsFlow()

    init {
        viewModelScope.launch {
            _feedType.collect {
                if (it != null) {
                    getFeed()
                }
            }
        }

        viewModelScope.launch {
            _instrumentFeeds.collectIndexed { _, value ->
                _feedItems.value =
                    convertFeedsToItemsUseCase(
                        ConvertFeedData(
                            value,
                            _pageState.value
                        )
                    ).successOr(
                        mutableListOf()
                    )
            }
        }

        viewModelScope.launch {
            _feedsState.collectIndexed { _, value ->
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(value, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }

        viewModelScope.launch {
            _followFeedsState.collectIndexed { _, value ->
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(value, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }

        viewModelScope.launch {
            _copyTradeFeedState.collectIndexed { _, value ->
                _feedItems.value =
                    convertFeedsToItemsUseCase(ConvertFeedData(value, _pageState.value)).successOr(
                        mutableListOf()
                    )
            }
        }
    }

    private suspend fun getFeed() {
        if (_pageState.value > 0) {
            getFeedsUseCase(
                FeedRequest(
                    _pageState.value,
                    _feedType.value!!,
                    _prefix.value
                )
            ).collectLatest { result ->
                val data = result.successOr(null)
                _pageState.value = data?.page ?: 0
                data?.feeds?.let { newFeeds ->
                    val feeds = when (_feedType.value) {
                        "" -> _feedsState.value.toMutableList()
                        "following" -> _followFeedsState.value.toMutableList()
                        "copy" -> _copyTradeFeedState.value.toMutableList()
                        "instrument" -> _instrumentFeeds.value.toMutableList()
                        else -> mutableListOf()
                    }
                    feeds.addAll(newFeeds)
                    when (_feedType.value) {
                        "" -> _feedsState.value = feeds
                        "following" -> _followFeedsState.value = feeds
                        "copy" -> _copyTradeFeedState.value = feeds
                        "instrument" -> _instrumentFeeds.value = feeds
                        else -> {}
                    }
                }
            }
        }
    }

    fun setFeedType(type: String = "", prefix: String = "") {
        refresh()
        _prefix.value = prefix
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
        _followFeedsState.value = mutableListOf()
        _copyTradeFeedState.value = mutableListOf()
        _userFeeds.value = mutableListOf()
        _instrumentFeeds.value = mutableListOf()
    }

    fun goToTop() {
        viewModelScope.launch {
            _goToTopState.send(Unit)
        }
    }

}