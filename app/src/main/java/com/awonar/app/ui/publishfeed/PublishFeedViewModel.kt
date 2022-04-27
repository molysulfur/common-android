package com.awonar.app.ui.publishfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.feed.CreateFeed
import com.awonar.android.model.feed.UserTag
import com.awonar.android.shared.domain.feed.CreateFeedUseCase
import com.awonar.android.shared.domain.feed.GetUserTagSuggestionUseCase
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PublishFeedViewModel @Inject constructor(
    private val getUserTagSuggestionUseCase: GetUserTagSuggestionUseCase,
    private val createFeedUseCase: CreateFeedUseCase
) : ViewModel() {

    private val _editableText = MutableStateFlow("")

    private val _suggestionState = Channel<SuggestionsResult>(Channel.CONFLATED)
    val suggestionState get() = _suggestionState.receiveAsFlow()

    private val _loadingState = Channel<Boolean>(Channel.CONFLATED)
    val loadingState get() = _loadingState.receiveAsFlow()

    fun getSuggestion(queryToken: QueryToken) {
        viewModelScope.launch {
            val keywords = queryToken.keywords
            val useCase = getUserTagSuggestionUseCase(keywords)
            useCase.collectIndexed { _, result ->
                val data = result.successOr(listOf())
                val suggestions = data?.map {
                    MentionSuggestion(it.username ?: "")
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

            createFeedUseCase(request).collectIndexed { index, result ->
                if (result is Result.Loading) {
                    _loadingState.send(true)
                }
                if (result !is Result.Loading) {
                    _loadingState.send(false)
                }
            }

        }
    }

    fun saveDaft(text: CharSequence?) {
        _editableText.value = "$text"
    }
}
