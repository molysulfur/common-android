package com.awonar.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.shared.domain.feed.GetAllFeedsUseCase
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
) : ViewModel() {

    val _pageState = MutableStateFlow(1)

    private val _feedsState = MutableStateFlow<MutableList<Feed?>>(mutableListOf())
    val feedsState get() = _feedsState

    init {
        viewModelScope.launch {
            _pageState.collect { page ->
                getAllFeedsUseCase(page).collectLatest { result ->
                    val data = result.successOr(null)
                    data?.feeds?.let { newFeeds ->
                        val feeds = _feedsState.value.toMutableList()

                        feeds.addAll(newFeeds)
                        _feedsState.value = feeds
                    }
                }
            }
        }
    }

}