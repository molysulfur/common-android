package com.awonar.app.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.domain.watchlist.AddWatchlistFolderUseCase
import com.awonar.android.shared.domain.watchlist.DeleteWatchlistFolderUseCase
import com.awonar.android.shared.domain.watchlist.GetWatchlistFoldersUseCase
import com.awonar.android.shared.utils.JsonUtil
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchlistFoldersUseCase: GetWatchlistFoldersUseCase,
    private val addWatchlistFolderUseCase: AddWatchlistFolderUseCase,
    private val deleteWatchlistFolderUseCase: DeleteWatchlistFolderUseCase,
) : ViewModel() {

    private val _navigateAction = Channel<String>(Channel.CONFLATED)
    val navigateAction get() = _navigateAction.receiveAsFlow()

    private val _progress = MutableStateFlow(false)
    val progress get() = _progress

    private val _folders = MutableStateFlow(emptyList<Folder>())
    val folders get() = _folders
    private val _watchlist = MutableStateFlow(mutableListOf<WatchlistItem>())
    val watchlist get() = _watchlist

    private val _finished = Channel<Unit>(Channel.CONFLATED)
    val finished get() = _finished.receiveAsFlow()
    private val _errorState = Channel<String>(Channel.CONFLATED)
    val errorState get() = _errorState.receiveAsFlow()

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

    fun addFolder(name: String) {
        viewModelScope.launch {
            addWatchlistFolderUseCase(name).collectLatest {
                if (it.succeeded) {
                    _finished.send(Unit)
                }
                if (it is Result.Error) {
                    val message = JsonUtil.getError(it.exception.message)
                    _errorState.send(message)
                }
            }
        }
    }

    fun deleteFolder(id: String) {
        viewModelScope.launch {
            deleteWatchlistFolderUseCase(id).collectLatest {
                if (it.succeeded) {
                    _folders.emit(it.successOr(emptyList()))
                }
                if (it is Result.Error) {
                    val message = JsonUtil.getError(it.exception.message)
                    _errorState.send(message)
                }
            }
        }
    }

    fun refresh(needFresh: Boolean) {
        viewModelScope.launch {
            getWatchlistFoldersUseCase(needFresh).collect {
                val folder = it.successOr(emptyList())
                _progress.value = it is Result.Loading
                _folders.emit(folder)
            }
        }
    }

    fun navigate(id: String) {
        viewModelScope.launch {
            _navigateAction.send(id)
        }
    }

    fun getWatchlist(watchlistId: String) {
        viewModelScope.launch {
            val folder = _folders.value.first { it.id == watchlistId }
            val list = mutableListOf<WatchlistItem>()
            list.add(WatchlistItem.ColumnItem(
                "Market",
                "Sell",
                "Buy"
            ))
            folder.infos.filter { it.type == "instrument" }.forEach { info ->
                list.add(
                    WatchlistItem.InstrumentItem(
                        instrumentId = info.instrumentId,
                        title = info.title,
                        image = info.image
                    )
                )
            }
            list.add(WatchlistItem.ColumnItem(
                "People",
                "Risk",
                "Change"
            ))
            folder.infos.filter { it.type == "user" }.forEach { info ->
                list.add(
                    WatchlistItem.TraderItem(
                        uid = info.uid,
                        title = info.title,
                        subTitle = info.subTitle,
                        image = info.image,
                        risk = info.risk,
                        gain = info.gain
                    )
                )
            }
            _watchlist.emit(list)
        }
    }

}