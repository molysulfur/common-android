package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.GetConversionsUseCase
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.portfolio.adapter.ColumnValue
import com.awonar.app.ui.portfolio.adapter.ColumnValueType
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PortFolioViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val getPositionOrderUseCase: GetPositionOrderUseCase,
    private var getPortfolioActivedColumnPreferenceUseCase: GetPortfolioActivedColumnPreferenceUseCase,
    private var getPortfolioColumnListUseCase: GetPortfolioColumnListUseCase,
    private var updatePortfolioColumnUseCase: UpdatePortfolioColumnUseCase,
    private var resetPortfolioColumnUseCase: ResetPortfolioColumnUseCase,
    private var calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private var getConversionByInstrumentUseCase: GetConversionByInstrumentUseCase,
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

    init {
        viewModelScope.launch {
            val list =
                getPortfolioActivedColumnPreferenceUseCase(Unit).successOr(emptyList())
            _activedColumnState.emit(list)
        }

        viewModelScope.launch {
            getPositionOrderUseCase(Unit).collect { result ->
                val itemList = mutableListOf<OrderPortfolioItem>()
                val column = _activedColumnState.value
                val instrumentIdList = arrayListOf<Int>()
                result.successOr(emptyList()).forEach {
                   val conversionRate :Float = getConversionByInstrumentUseCase(it.instrumentId).successOr(0f)
                    instrumentIdList.add(it.instrumentId)
                    itemList.add(
                        OrderPortfolioItem.InstrumentPortfolioItem(
                            position = it,
                            conversionRate = conversionRate,
                            value1 = getValueFromActivedColumn(it, column[0]),
                            value2 = getValueFromActivedColumn(it, column[1]),
                            value3 = getValueFromActivedColumn(it, column[2]),
                            value4 = getValueFromActivedColumn(it, column[3])
                        )
                    )
                }
                _subscricbeQuote.send(instrumentIdList)
                _positionOrderList.emit(itemList)
            }
        }
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
            "SL(%)" -> ColumnValue(0f, ColumnValueType.COLUMN_TEXT)
            "TP(%)" -> ColumnValue(0f, ColumnValueType.COLUMN_TEXT)
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
            when (isDesc) {
                true -> itemList.sortByDescending {
                    when (index) {
                        1 -> it.value1?.value
                        2 -> it.value2?.value
                        3 -> it.value3?.value
                        else -> it.value4?.value
                    }
                }
                else -> itemList.sortBy {
                    when (index) {
                        1 -> it.value1?.value
                        2 -> it.value2?.value
                        3 -> it.value3?.value
                        else -> it.value4?.value
                    }
                }
            }
            _positionOrderList.emit(itemList)
        }
    }
}