package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.portfolio.ConvertCopierToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
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
    private val getPositionManualUseCase: GetPositionManualUseCase,
    private var getActivedManualColumnUseCase: GetActivedManualColumnUseCase,
    private var getActivedMarketColumnUseCase: GetActivedMarketColumnUseCase,
    private var getManualColumnListUseCase: GetManualColumnListUseCase,
    private var getMarketColumnListUseCase: GetMarketColumnListUseCase,
    private var updateManualColumnUseCase: UpdateManualColumnUseCase,
    private var updateMarketColumnUseCase: UpdateMarketColumnUseCase,
    private var resetManualColumnUseCase: ResetManualColumnUseCase,
    private var resetMarketColumnUseCase: ResetMarketColumnUseCase,
    private var getPositionMarketUseCase: GetPositionMarketUseCase,
    private var convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private var convertCopierToItemUseCase: ConvertCopierToItemUseCase,
) : ViewModel() {

    private val _portfolioType = MutableStateFlow("market")
    val portfolioType: StateFlow<String> get() = _portfolioType

    private val _navigateActivedColumn = Channel<String>(capacity = Channel.CONFLATED)
    val navigateActivedColumn: Flow<String> = _navigateActivedColumn.receiveAsFlow()

    private val _sortColumnState = Channel<Pair<String, Boolean>>(capacity = Channel.CONFLATED)
    val sortColumnState: Flow<Pair<String, Boolean>> = _sortColumnState.receiveAsFlow()

    private val _subscricbeQuote = Channel<List<Int>>(capacity = Channel.CONFLATED)
    val subscricbeQuote: Flow<List<Int>> = _subscricbeQuote.receiveAsFlow()

    private val _activedColumnState: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val activedColumnState: StateFlow<List<String>> get() = _activedColumnState

    val columnState: StateFlow<List<String>> = flow {
        val list = when (_portfolioType.value) {
            "manual" -> getManualColumnListUseCase(_activedColumnState.value).successOr(
                emptyList()
            )
            else -> getMarketColumnListUseCase(_activedColumnState.value).successOr(emptyList())
        }
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
            val list = getActivedMarketColumnUseCase(Unit).successOr(emptyList())
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
        val copierItems = convertCopierToItemUseCase(copies).successOr(emptyList())
        itemList.addAll(copierItems)
        _positionMarketState.emit(itemList)
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
            when (_portfolioType.value) {
                "manual" -> updateManualColumnUseCase(activedList)
                "market" -> updateMarketColumnUseCase(activedList)
            }
        }
    }

    fun resetActivedColumn() {
        viewModelScope.launch {
            when (_portfolioType.value) {
                "manual" -> resetManualColumnUseCase(Unit)
                "market" -> resetMarketColumnUseCase(Unit)
            }
        }
    }

    fun sortColumn(coloumn: String, isDesc: Boolean) {
        viewModelScope.launch {
            _sortColumnState.send(Pair(coloumn, isDesc))
        }
    }

    fun getActivedColoumn(type: String) {
        viewModelScope.launch {
            val actived = when (type) {
                "market" -> getActivedMarketColumnUseCase(Unit).successOr(emptyList())
                "manual" -> getActivedManualColumnUseCase(Unit).successOr(emptyList())
                else -> emptyList()
            }
            _activedColumnState.emit(actived)
        }
    }

    fun togglePortfolio(type: String) {
        viewModelScope.launch {
            _portfolioType.emit(type)
        }
    }


}