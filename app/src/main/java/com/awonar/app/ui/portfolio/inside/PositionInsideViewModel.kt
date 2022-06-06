package com.awonar.app.ui.portfolio.inside

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.domain.portfolio.*
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PositionInsideViewModel @Inject constructor(
    private val convertInsideCopierPositionToItemUseCase: ConvertInsideCopierPositionToItemUseCase,
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private val convertPublicPositionToItemUseCase: ConvertPublicPositionToItemUseCase,
) : ViewModel() {

    private val _sumFloatingPL = MutableStateFlow(0f)
    val sumFloatingPL get() = _sumFloatingPL
    private val _sumValue = MutableStateFlow(0f)
    val sumValue get() = _sumValue

    private val _editDialog = Channel<Position?>(capacity = Channel.CONFLATED)
    val editDialog: Flow<Position?> get() = _editDialog.receiveAsFlow()
    private val _closeDialog = Channel<Position?>(capacity = Channel.CONFLATED)
    val closeDialog: Flow<Position?> get() = _closeDialog.receiveAsFlow()
    private val _positionItems: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<PortfolioItem>> get() = _positionItems

    fun convertPublicPosition(positions: List<Position>) {
        viewModelScope.launch {
            val itemList = convertPublicPositionToItemUseCase(positions).successOr(mutableListOf())
            _positionItems.emit(itemList)
        }
    }

    fun convertPosition(userPosition: UserPortfolioResponse?, item: PortfolioItem.PositionItem) {
        viewModelScope.launch {
            userPosition?.let {
                val items = convertPositionToItemUseCase(
                    ConvertInsidePosition(
                        userPosition,
                        item
                    )
                ).successOr(emptyList())
                _positionItems.value = items.toMutableList()
            }
        }
    }

    fun getCopiesWithIndex(
        userPosition: UserPortfolioResponse?,
        currentIndex: PortfolioItem.CopierPortfolioItem
    ) {
        viewModelScope.launch {
            val itemLists =
                convertInsideCopierPositionToItemUseCase(currentIndex.copier).successOr(emptyList())
            _positionItems.value = itemLists.toMutableList()
        }
    }

    fun showCloseDialog(position: Int) {
        viewModelScope.launch {
            val item = _positionItems.value[position]
            when (item) {
                is PortfolioItem.PositionItem -> {
//                    _closeDialog.send(item.position)
                }
                else -> {}
            }
        }
    }

    fun showEditDialog(position: Int) {
        viewModelScope.launch {
            val item = _positionItems.value[position]
            when (item) {
//                is PortfolioItem.InstrumentPortfolioItem -> {
//                    _editDialog.send(item.position)
//                }
                else -> {}
            }
        }
    }

    fun convertPositionWithCopies(copies: Copier?, currentIndex: Int) {
//        viewModelScope.launch {
//            copies?.let { copies ->
//                val position = copies.positions?.get(currentIndex)
//                position?.let { position ->
//                    val items =
//                        convertPositionToItemUseCase().successOr(
//                            emptyList()
//                        )
//                    _positionState.value = position
//                    _positionItems.value = items.toMutableList()
//                }
//            }
//        }
    }


}