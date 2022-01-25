package com.awonar.app.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.domain.watchlist.GetWatchlistFoldersUseCase
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchlistFoldersUseCase: GetWatchlistFoldersUseCase,
) : ViewModel() {

    private val _progress = MutableStateFlow(false)
    val progress get() = _progress

    private val _folders = MutableStateFlow(emptyList<Folder>())
    val folders get() = _folders

    init {
        val folders = flow {
            getWatchlistFoldersUseCase(true).collect {
                val folder = it.successOr(emptyList())
                _progress.value = it is Result.Loading
                emit(folder)
            }
        }
        viewModelScope.launch {
            folders.collect {
                _folders.emit(it)
            }
        }

    }
}