package com.awonar.app.ui.portfolio.inside

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.app.domain.portfolio.ConvertGroupPositionToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionInsideViewModel @Inject constructor(
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private val convertPositionGroupPositionToItemUseCase: ConvertGroupPositionToItemUseCase,
) : ViewModel() {

    private val _editDialog = Channel<Position?>(capacity = Channel.CONFLATED)
    val editDialog: Flow<Position?> get() = _editDialog.receiveAsFlow()
    private val _closeDialog = Channel<Position?>(capacity = Channel.CONFLATED)
    val closeDialog: Flow<Position?> get() = _closeDialog.receiveAsFlow()

    private val _positionState: MutableStateFlow<Position?> = MutableStateFlow(null)
    val positionState: StateFlow<Position?> get() = _positionState

    private val _copiesState: MutableStateFlow<Copier?> = MutableStateFlow(null)
    val copiesState: StateFlow<Copier?> get() = _copiesState

    private val _positionItems: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<PortfolioItem>> get() = _positionItems

    fun convertPosition(userPosition: UserPortfolioResponse?, index: Int) {
        viewModelScope.launch {
            val position: Position? = userPosition?.positions?.get(index)
            val positionList: List<Position> =
                userPosition?.positions?.filter { it.instrument.id == position?.instrument?.id }
                    ?: emptyList()
            if (positionList.isNotEmpty()) {
                val items = convertPositionToItemUseCase(positionList).successOr(emptyList())
                _positionState.value = positionList[0]
                _positionItems.value = items.toMutableList()
            }
        }
    }

    fun convertCopies(userPosition: UserPortfolioResponse?, currentIndex: Int) {
        viewModelScope.launch {
            val copies: Copier? = userPosition?.copies?.get(currentIndex)
            val positionList: List<Position> = copies?.positions ?: emptyList()
            ?: emptyList()
            if (copies != null) {
                val items =
                    convertPositionGroupPositionToItemUseCase(positionList).successOr(emptyList())
                        .toMutableList()
                items.add(0, PortfolioItem.SectionItem("Start Copy ${DateUtils.getDate(copies.startedCopyDate)}"))
                _copiesState.value = copies
                _positionItems.value = items
            }
        }
    }

    fun showCloseDialog(position: Int) {
        viewModelScope.launch {
            val item = _positionItems.value[position]
            when (item) {
                is PortfolioItem.InstrumentPortfolioItem -> {
                    _closeDialog.send(item.position)
                }
                else -> {}
            }
        }
    }

    fun showEditDialog(position: Int) {
        viewModelScope.launch {
            val item = _positionItems.value[position]
            when (item) {
                is PortfolioItem.InstrumentPortfolioItem -> {
                    _editDialog.send(item.position)
                }
                else -> {}
            }
        }
    }

    fun convertPositionWithCopies(copies: Copier?, currentIndex: Int) {
        viewModelScope.launch {
            copies?.let { copies ->
                val position = copies.positions?.get(currentIndex)
                position?.let { position ->
                    val items =
                        convertPositionToItemUseCase(copies.positions?.filter { it.instrument.id == position.instrument.id }
                            ?: emptyList()).successOr(
                            emptyList())
                    _positionState.value = position
                    _positionItems.value = items.toMutableList()
                }
            }
        }
    }

}