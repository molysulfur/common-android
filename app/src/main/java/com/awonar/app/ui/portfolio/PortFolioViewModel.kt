package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.portfolio.ConvertCopierToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.adapter.ColumnValue
import com.awonar.app.ui.portfolio.adapter.ColumnValueType
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PortFolioViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val getPositionManualUseCase: GetPositionManualUseCase,
    private var getPortfolioActivedColumnPreferenceUseCase: GetPortfolioActivedColumnPreferenceUseCase,
    private var getPortfolioColumnListUseCase: GetPortfolioColumnListUseCase,
    private var updatePortfolioColumnUseCase: UpdatePortfolioColumnUseCase,
    private var resetPortfolioColumnUseCase: ResetPortfolioColumnUseCase,
    private var calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private var getPositionMarketUseCase: GetPositionMarketUseCase,
    private var convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private var convertCopierToItemUseCase: ConvertCopierToItemUseCase,
) : ViewModel() {

    private val _navigateActivedColumn = Channel<String>(capacity = Channel.CONFLATED)
    val navigateActivedColumn: Flow<String> = _navigateActivedColumn.receiveAsFlow()

    private val _subscricbeQuote = Channel<List<Int>>(capacity = Channel.CONFLATED)
    val subscricbeQuote: Flow<List<Int>> = _subscricbeQuote.receiveAsFlow()

    private val _activedColumnState: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val activedColumnState: StateFlow<List<String>> get() = _activedColumnState

    val columnState: StateFlow<List<String>> = flow {
        val list = getPortfolioColumnListUseCase(_activedColumnState.value).successOr(emptyList())
        emit(list)
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val portfolioState: StateFlow<Portfolio?> = flow {
        getMyPortFolioUseCase(true).collect {
            this.emit(it.successOr(null))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _positionOrderList: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf())
    val positionOrderList: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionOrderList

    private val _positionMarketState: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf())
    val positionMarketState: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionMarketState

    init {

        viewModelScope.launch {
            getPositionMarketUseCase(Unit).collect {
                val data = it.successOr(null)
                if (data != null) {
                    convertToItem(data.positions, data.copies)
                }
            }
        }
        viewModelScope.launch {
            val list =
                getPortfolioActivedColumnPreferenceUseCase(Unit).successOr(emptyList())
            _activedColumnState.emit(list)
        }

        viewModelScope.launch {
            getPositionManualUseCase(Unit).collect { result ->
                val positionItemResult =
                    convertPositionToItemUseCase(result.successOr(emptyList())).successOr(emptyList())
                _subscricbeQuote.send(result.successOr(emptyList()).map { it.instrumentId })
                _positionOrderList.emit(positionItemResult.toMutableList())
            }
        }
    }

    private suspend fun convertToItem(positions: List<Position>, copies: List<Copier>) {
        val itemList = mutableListOf<OrderPortfolioItem>()
        val positionItems = convertPositionToItemUseCase(positions).successOr(emptyList())
        itemList.addAll(positionItems)
        Timber.e("positionItems ${positionItems}")
        val copierItems = convertCopierToItemUseCase(copies).successOr(emptyList())
        itemList.addAll(copierItems)
        Timber.e("copierItems ${copierItems}")
        _positionMarketState.emit(itemList)
    }

    private suspend fun getValueFromActivedColumn(position: Position, column: String): ColumnValue =
        when (column) {
            "Invested" -> ColumnValue(position.amount, ColumnValueType.COLUMN_TEXT)
            "Units" -> ColumnValue(position.units, ColumnValueType.COLUMN_TEXT)
            "Open" -> ColumnValue(position.openRate, ColumnValueType.COLUMN_TEXT)
            "Current" -> ColumnValue(0f, ColumnValueType.COLUMN_CURRENT)
            "S/L($)" -> ColumnValue(0f, ColumnValueType.PROFITLOSS)
            "S/L(%)" -> ColumnValue(0f, ColumnValueType.PROFITLOSS_PERCENT)
            "Pip Change" -> ColumnValue(0f, ColumnValueType.COLUMN_PIP_CHANGE)
            "Leverage" -> ColumnValue(position.leverage.toFloat(), ColumnValueType.COLUMN_TEXT)
            "Value" -> ColumnValue(0f, ColumnValueType.COLUMN_VALUE)
            "Fee" -> ColumnValue(position.totalFees, ColumnValueType.COLUMN_TEXT)
            "Execute at" -> ColumnValue(position.amount, ColumnValueType.COLUMN_TEXT)
            "SL" -> ColumnValue(position.stopLossRate, ColumnValueType.COLUMN_TEXT)
            "TP" -> ColumnValue(position.takeProfitRate, ColumnValueType.COLUMN_TEXT)
            "SL($)" -> ColumnValue(
                calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        position.instrumentId,
                        Price(0f, position.stopLossRate, "amount"),
                        position.openRate,
                        position.units
                    )
                ).successOr(
                    Price(0f, 0f, "amount")
                ).amount, ColumnValueType.COLUMN_TEXT
            )
            "TP($)" -> ColumnValue(
                calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        position.instrumentId,
                        Price(0f, position.takeProfitRate, "amount"),
                        position.openRate,
                        position.units
                    )
                ).successOr(
                    Price(0f, 0f, "amount")
                ).amount, ColumnValueType.COLUMN_TEXT
            )
            "SL(%)" -> {
                val amountSL = calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        position.instrumentId,
                        Price(0f, position.stopLossRate, "amount"),
                        position.openRate,
                        position.units
                    )
                ).successOr(
                    Price(0f, 0f, "amount")
                ).amount
                ColumnValue(amountSL.times(100).div(position.amount), ColumnValueType.COLUMN_TEXT)
            }
            "TP(%)" -> {
                val amountTp = calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        position.instrumentId,
                        Price(0f, position.takeProfitRate, "amount"),
                        position.openRate,
                        position.units
                    )
                ).successOr(
                    Price(0f, 0f, "amount")
                ).amount
                ColumnValue(amountTp.times(100).div(position.amount), ColumnValueType.COLUMN_TEXT)
            }
            else -> throw Error("column name is not found!")
        }

    fun activedColumnChange(text: String) {
        viewModelScope.launch {
            _navigateActivedColumn.send(text)
        }
    }

    fun replaceActivedColumn(oldColumn: String, newColumn: String) {
        viewModelScope.launch {
            val activedList = _activedColumnState.value.toMutableList()
            val index = activedList.indexOf(oldColumn)
            activedList[index] = newColumn
            _activedColumnState.emit(activedList)
        }
    }

    fun saveActivedColumn() {
        viewModelScope.launch {
            val activedList = _activedColumnState.value.toMutableList()
            updatePortfolioColumnUseCase(activedList)
        }
    }

    fun resetActivedColumn() {
        viewModelScope.launch {
            resetPortfolioColumnUseCase(Unit)
        }
    }

    fun sortColumn(index: Int, isDesc: Boolean) {
        viewModelScope.launch {
            val itemList: MutableList<OrderPortfolioItem> = _positionOrderList.value.toMutableList()
//            when (isDesc) {
//                true -> itemList.sortByDescending {
//                    when (index) {
//                        1 -> it.value1?.value
//                        2 -> it.value2?.value
//                        3 -> it.value3?.value
//                        else -> it.value4?.value
//                    }
//                }
//                else -> itemList.sortBy {
//                    when (index) {
//                        1 -> it.value1?.value
//                        2 -> it.value2?.value
//                        3 -> it.value3?.value
//                        else -> it.value4?.value
//                    }
//                }
//            }
            _positionOrderList.emit(itemList)
        }
    }
}