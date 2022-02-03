package com.awonar.app.ui.watchlist

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.watchlist.AddWatchlistItemRequest
import com.awonar.android.model.watchlist.DeleteWatchlistRequest
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.domain.watchlist.*
import com.awonar.android.shared.utils.JsonUtil
import com.awonar.app.domain.watchlist.ConvertWatchlistItemUseCase
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
    private val deleteWatchlistUseCase: DeleteWatchlistUseCase,
    private val addWatchlistUseCase: AddWatchlistUseCase,
    private val convertWatchlistItemUseCase: ConvertWatchlistItemUseCase,
    private val updateDefaultFolderUseCase: UpdateDefaultFolderUseCase,
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title get() = _title

    private val _showDialog = Channel<String>(Channel.CONFLATED)
    val showDialog get() = _showDialog.receiveAsFlow()

    private val _navigateAction = Channel<NavDirections>(Channel.CONFLATED)
    val navigateAction get() = _navigateAction.receiveAsFlow()

    private val _openActivity = Channel<String>(Channel.CONFLATED)
    val openActivity get() = _openActivity.receiveAsFlow()

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
    private val _successState = Channel<String>(Channel.CONFLATED)
    val successState get() = _successState.receiveAsFlow()

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

    fun convertItemsWithInstrumentExsit(instruments: List<Instrument>, folderId: String?) {
        _progress.value = true
        val itemLists = mutableListOf<WatchlistItem>()
        instruments.forEach { instrument ->
            itemLists.add(
                WatchlistItem.SelectorItem(
                    instrumentId = instrument.id,
                    image = instrument.logo,
                    title = instrument.symbol,
                    isSelected = _folders.value.find { it.id == folderId }?.infos?.find { it.instrumentId == instrument.id } != null
                )
            )
        }
        _progress.value = false
        _watchlist.value = itemLists.toMutableList()
    }

    fun addFolder(name: String) {
        viewModelScope.launch {
            addWatchlistFolderUseCase(name).collectLatest {
                if (it.succeeded) {
                    _finished.send(Unit)
                } else if (it is Result.Error) {
                    val message = JsonUtil.getError(it.exception.message)
                    _errorState.send(message)
                }
            }
        }
    }

    fun deleteFolder(id: String) {
        viewModelScope.launch {
            if (_folders.value.find { it.id == id }?.default == true) {
                _errorState.send("Default Folder cann't removed.")
            } else {
                deleteWatchlistFolderUseCase(id).collectLatest {
                    if (it.succeeded) {
                        _folders.emit(it.successOr(emptyList()))
                    } else if (it is Result.Error) {
                        val message = JsonUtil.getError(it.exception.message)
                        _errorState.send(message)
                    }
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

    fun navigate(id: NavDirections) {
        viewModelScope.launch {
            _navigateAction.send(id)
        }
    }

    fun getWatchlist(watchlistId: String) {
        viewModelScope.launch {
            val folder = _folders.value.find { it.id == watchlistId }
            val list =
                convertWatchlistItemUseCase(folder?.infos ?: emptyList()).successOr(mutableListOf())
            _title.value = folder?.name ?: ""
            _watchlist.value = list.toMutableList()
        }
    }

    fun addItem(id: Int, folderId: String?) {
        viewModelScope.launch {
            val folder = _folders.value.find { it.id == folderId }
            id.let { _ ->
                addWatchlistUseCase(AddWatchlistItemRequest(
                    folderId = folder?.id ?: "",
                    instrumentId = id
                )).collect {
                    if (it.succeeded) {
                        _folders.emit(it.successOr(emptyList()))
                        _successState.send("")
                    } else if (it is Result.Error) {
                        val message = JsonUtil.getError(it.exception.message)
                        _errorState.send(message)
                    }
                }
            }
        }
    }

    fun removeItem(instrumentId: Int, folderId: String?) {
        viewModelScope.launch {
            val folder = _folders.value.find { it.id == folderId }
            val id = folder?.infos?.find { it.instrumentId == instrumentId }?.id
            id?.let { _ ->
                deleteWatchlistUseCase(DeleteWatchlistRequest(
                    folderId = folder.id,
                    itemId = id
                )).collect {
                    if (it.succeeded) {
                        _folders.emit(it.successOr(emptyList()))
                        _successState.send("")
                    } else if (it is Result.Error) {
                        val message = JsonUtil.getError(it.exception.message)
                        _errorState.send(message)
                    }
                }
            }
        }
    }

    fun removeItem(position: Int) {
        viewModelScope.launch {
            val id = when (val item = _watchlist.value[position]) {
                is WatchlistItem.InstrumentItem -> item.id
                is WatchlistItem.TraderItem -> item.id
                else -> null
            }
            val folder = _folders.value.find { it.infos.find { it.id == id } != null }
            id?.let { _ ->
                deleteWatchlistUseCase(DeleteWatchlistRequest(
                    folderId = folder?.id ?: "",
                    itemId = id
                )).collect {
                    if (it.succeeded) {
                        val newFolders = it.successOr(emptyList())
                        _folders.emit(newFolders)
                        folder?.id?.let { id ->
                            getWatchlist(id)
                        }
                    } else if (it is Result.Error) {
                        val message = JsonUtil.getError(it.exception.message)
                        _errorState.send(message)
                    }
                }
            }
        }
    }

    fun show() {
        viewModelScope.launch {
            _showDialog.send("")
        }
    }

    fun setDefault(folderId: String) {
        viewModelScope.launch {
            updateDefaultFolderUseCase(folderId).collect {
                if (it.succeeded) {
                    _folders.emit(it.successOr(emptyList()))
                } else if (it is Result.Error) {
                    val message = JsonUtil.getError(it.exception.message)
                    _errorState.send(message)
                }
            }
        }
    }

    fun getWatchlistDefault() {
        viewModelScope.launch {
            val folder = _folders.value.find { it.default }
            val list =
                convertWatchlistItemUseCase(folder?.infos ?: emptyList()).successOr(mutableListOf())
            _title.value = folder?.name ?: ""
            _watchlist.value = list.toMutableList()
        }
    }

    fun getWatchlistRecently() {
        viewModelScope.launch {
            val folder = _folders.value.find { it.recentlyInvested }
            val list =
                convertWatchlistItemUseCase(folder?.infos ?: emptyList()).successOr(mutableListOf())
            _watchlist.value = list.toMutableList()
        }
    }

    fun setTitle(text: String) {
        _title.value = text
    }

    fun openAddWatchlist() {
        viewModelScope.launch {
            _openActivity.send("add")
        }
    }

}