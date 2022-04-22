package com.awonar.app.ui.publishfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.UserTag
import com.awonar.android.shared.domain.feed.GetUserTagSuggestionUseCase
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishFeedViewModel @Inject constructor(
    private val getUserTagSuggestionUseCase: GetUserTagSuggestionUseCase,
) : ViewModel() {

    private val _suggestionState = Channel<MutableList<MentionSuggestion>>(Channel.CONFLATED)
    val suggestionState get() = _suggestionState.receiveAsFlow()

    fun getSuggestion(keywords: String): Flow<Result<List<UserTag>?>> {
        return getUserTagSuggestionUseCase(keywords)
//        viewModelScope.launch {
//            getUserTagSuggestionUseCase(keywords).collectIndexed { _, value ->
//                val data = value.successOr(listOf())
//                val suggestionResult = data?.map {
//                    MentionSuggestion(it.username ?: "")
//                }
//                _suggestionState.send(suggestionResult?.toMutableList() ?: mutableListOf())
//            }
//
//        }
    }
}
