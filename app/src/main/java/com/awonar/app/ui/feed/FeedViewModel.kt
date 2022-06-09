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
    private val _profileFeedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
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
            _profileFeedsState.collectIndexed { _, value ->
                Timber.e("${_feedType.value} convert : ${_pageState.value}")
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
        Timber.e("${_feedType.value} getFeed : ${_pageState.value}")
        if (_pageState.value > 0 && _feedType.value != null) {
            getFeedsUseCase(
                FeedRequest(
                    _pageState.value,
                    _feedType.value!!,
                    _prefix.value
                )
            ).collectLatest { result ->
                val data = result.successOr(null)
                _pageState.value = data?.page ?: 0
                val feeds = when (_feedType.value) {
                    "" -> _feedsState.value.toMutableList()
                    "following" -> _followFeedsState.value.toMutableList()
                    "copy" -> _copyTradeFeedState.value.toMutableList()
                    "instrument", "user" -> _profileFeedsState.value.toMutableList()
                    else -> mutableListOf()
                }
                feeds.addAll(data?.feeds ?: mutableListOf())
                when (_feedType.value) {
                    "" -> _feedsState.value = feeds
                    "following" -> _followFeedsState.value = feeds
                    "copy" -> _copyTradeFeedState.value = feeds
                    "instrument", "user" -> _profileFeedsState.value = feeds
                    else -> {}
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
        _profileFeedsState.value = mutableListOf()
    }

    fun goToTop() {
        viewModelScope.launch {
            _goToTopState.send(Unit)
        }
    }

}