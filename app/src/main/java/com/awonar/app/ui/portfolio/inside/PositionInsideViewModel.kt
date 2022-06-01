package com.awonar.app.ui.portfolio.inside

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.domain.portfolio.ConvertMarketToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPublicPositionToItemUseCase
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
    private val convertPositionGroupPositionToItemUseCase: ConvertMarketToItemUseCase,
    private val convertPublicPositionToItemUseCase: ConvertPublicPositionToItemUseCase,
    private val getConversionByInstrumentUseCase: GetConversionByInstrumentUseCase
) : ViewModel() {

    private val _sumFloatingPL = MutableStateFlow(0f)
    val sumFloatingPL get() = _sumFloatingPL
    private val _sumValue = MutableStateFlow(0f)
    val sumValue get() = _sumValue

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

    fun convertPublicPosition(positions: List<Position>) {
        viewModelScope.launch {
            val itemList = convertPublicPositionToItemUseCase(positions).successOr(mutableListOf())
            _positionItems.emit(itemList)
        }
    }

    fun convertPosition(userPosition: UserPortfolioResponse?, index: Int) {
        viewModelScope.launch {
            val position: Position? = userPosition?.positions?.get(index)
            val positionList: List<Position> =
                userPosition?.positions?.filter { it.instrument?.id == position?.instrument?.id }
                    ?: emptyList()
            if (positionList.isNotEmpty()) {
                val items = convertPositionToItemUseCase(positionList).successOr(emptyList())
                _positionState.value = positionList[0]
                _positionItems.value = items.toMutableList()
            }
        }
    }

    fun getCopiesWithIndex(userPosition: UserPortfolioResponse?, currentIndex: Int) {
        viewModelScope.launch {
            val copies: Copier? = userPosition?.copies?.get(currentIndex)
            val positionList: List<Position> = copies?.positions ?: emptyList()
            ?: emptyList()
            if (copies != null) {
//                val items =
//                    convertPositionGroupPositionToItemUseCase(positionList).successOr(emptyList())
//                        .toMutableList()
//                items.add(
//                    0,
//                    PortfolioItem.SectionItem("Start Copy ${DateUtils.getDate(copies.startedCopyDate)}")
//                )
//                _copiesState.value = copies
//                _positionItems.value = items
            }
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
        viewModelScope.launch {
            copies?.let { copies ->
                val position = copies.positions?.get(currentIndex)
                position?.let { position ->
                    val items =
                        convertPositionToItemUseCase(copies.positions?.filter { it.instrument?.id == position.instrument?.id }
                            ?: emptyList()).successOr(
                            emptyList()
                        )
                    _positionState.value = position
                    _positionItems.value = items.toMutableList()
                }
            }
        }
    }

    fun updateHeader(quotes: MutableMap<Int, Quote>) {
        viewModelScope.launch {
            val copier = _copiesState.value
            val sumPL = copier?.positions?.sumOf { position ->
                val conversion =
                    getConversionByInstrumentUseCase(position.instrument?.id ?: 0).successOr(0f)
                val quote = quotes[position.instrumentId]
                val current = if (position.isBuy) quote?.bid else quote?.ask
                val pl = PortfolioUtil.getProfitOrLoss(
                    current ?: 0f,
                    position.openRate,
                    position.units,
                    conversion,
                    position.isBuy
                ).toDouble()
                pl
            } ?: 0.0
            val pl = copier?.closedPositionsNetProfit?.plus(sumPL.toFloat()) ?: 0f
            _sumFloatingPL.value = pl
            val value = copier?.invested?.plus(pl) ?: 0f

            _sumValue.value = value
        }
    }

}