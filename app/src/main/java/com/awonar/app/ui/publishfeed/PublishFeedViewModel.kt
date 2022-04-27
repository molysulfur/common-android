package com.awonar.app.ui.publishfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.CreateFeed
import com.awonar.android.model.feed.Feed
import com.awonar.android.shared.domain.feed.CreateFeedUseCase
import com.awonar.android.shared.domain.feed.GetInstrumentTagSuggestionUseCase
import com.awonar.android.shared.domain.feed.GetUserTagSuggestionUseCase
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishFeedViewModel @Inject constructor(
    private val getUserTagSuggestionUseCase: GetUserTagSuggestionUseCase,
    private val getInstrumentTagSuggestionUseCase: GetInstrumentTagSuggestionUseCase,
    private val createFeedUseCase: CreateFeedUseCase
) : ViewModel() {

    private val _editableText = MutableStateFlow("")

    private val _suggestionState = Channel<SuggestionsResult>(Channel.CONFLATED)
    val suggestionState get() = _suggestionState.receiveAsFlow()
    private val _loadingState = Channel<Boolean>(Channel.CONFLATED)
    val loadingState get() = _loadingState.receiveAsFlow()
    private val _publishFeedState = Channel<Feed?>(Channel.CONFLATED)
    val publishFeedState get() = _publishFeedState.receiveAsFlow()

    fun getUserSuggestion(queryToken: QueryToken) {
        viewModelScope.launch {
            val keywords = queryToken.keywords
            val useCase = getUserTagSuggestionUseCase(keywords)
            useCase.collectIndexed { _, result ->
                val data = result.successOr(listOf())
                val suggestions = data?.map {
                    MentionSuggestion("@${it.username}")
                }
                val suggestion = SuggestionsResult(
                    queryToken,
                    suggestions?.toMutableList() ?: mutableListOf()
                )
                _suggestionState.send(suggestion)
            }
        }

    }

    fun submit() {
        viewModelScope.launch {
            val text = _editableText.value
            val request = CreateFeed(
                description = text
            )
            createFeedUseCase(request).collectIndexed { _, result ->
                if (result is Result.Loading) {
                    _loadingState.send(true)
                }
                if (result !is Result.Loading) {
                    _loadingState.send(false)
                    _publishFeedState.send(result.successOr(null))
                }
            }

        }
    }

    fun saveDaft(text: CharSequence?) {
        _editableText.value = "$text"
    }

    fun getInstrumentSuggestion(queryToken: QueryToken) {
        viewModelScope.launch {
            val keywords = queryToken.keywords
            val useCase = getInstrumentTagSuggestionUseCase(keywords)
            useCase.collectIndexed { _, result ->
                val data = result.successOr(listOf())
                val suggestions = data?.map {
                    MentionSuggestion("$${it.username}")
                }
                val suggestion = SuggestionsResult(
                    queryToken,
                    suggestions?.toMutableList() ?: mutableListOf()
                )
                _suggestionState.send(suggestion)
            }
        }

    }

    fun canBack(): Boolean {
        if (!_editableText.value.isNullOrBlank()) {
            return false
        }
        return true
    }
}
