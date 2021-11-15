package com.awonar.app.ui.columns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.domain.history.GetHistoryActivedColumnUseCase
import com.awonar.android.shared.domain.history.GetHistoryColumnUseCase
import com.awonar.android.shared.domain.history.ResetHistoryColumnUseCase
import com.awonar.android.shared.domain.history.UpdateHistoryColumnUseCase
import com.awonar.android.shared.domain.portfolio.*
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
class ColumnsViewModel @Inject constructor(
    private val getHistoryActivedColumnUseCase: GetHistoryActivedColumnUseCase,
    private val getHistoryColumnUseCase: GetHistoryColumnUseCase,
    private var updateManualColumnUseCase: UpdateManualColumnUseCase,
    private var updateMarketColumnUseCase: UpdateMarketColumnUseCase,
    private var resetManualColumnUseCase: ResetManualColumnUseCase,
    private var resetMarketColumnUseCase: ResetMarketColumnUseCase,
    private var updateHistoryColumnUseCase: UpdateHistoryColumnUseCase,
    private var resetHistoryColumnUseCase: ResetHistoryColumnUseCase,
    private var getManualColumnListUseCase: GetManualColumnListUseCase,
    private var getMarketColumnListUseCase: GetMarketColumnListUseCase,
    private var getActivedManualColumnUseCase: GetActivedManualColumnUseCase,
    private var getActivedMarketColumnUseCase: GetActivedMarketColumnUseCase
) : ViewModel() {

    private val _navigateActivedColumn = Channel<String>(capacity = Channel.CONFLATED)
    val navigateActivedColumn: Flow<String> = _navigateActivedColumn.receiveAsFlow()
    private val _activedColumnState = MutableStateFlow<List<String>>(emptyList())
    val activedColumnState: StateFlow<List<String>> get() = _activedColumnState

    private val _columnState = MutableStateFlow<List<String>>(emptyList())
    val columnState: StateFlow<List<String>> get() = _columnState

    private val _columnType = MutableStateFlow<String?>(null)

    fun setColumnType(type: String) {
        viewModelScope.launch {
            _columnType.emit(type)
            fetchActivedColumns()
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

    fun activedColumnChange(text: String) {
        viewModelScope.launch {
            _navigateActivedColumn.send(text)
        }
    }

    fun saveActivedColumn() {
        viewModelScope.launch {
            val activedList = _activedColumnState.value.toMutableList()
            when (_columnType.value) {
                "manual" -> updateManualColumnUseCase(activedList)
                "market" -> updateMarketColumnUseCase(activedList)
                "history" -> updateHistoryColumnUseCase(activedList)
            }
        }
    }

    fun resetActivedColumn() {
        viewModelScope.launch {
            when (_columnType.value) {
                "manual" -> resetManualColumnUseCase(Unit)
                "market" -> resetMarketColumnUseCase(Unit)
                "history" -> resetHistoryColumnUseCase(Unit)
            }
        }
    }

    fun getActivedColumns() {
        viewModelScope.launch {
            fetchActivedColumns()
        }
    }

    private suspend fun fetchActivedColumns() {
        val type = _columnType.value
        val data = when (type?.lowercase()) {
            "market" -> getActivedMarketColumnUseCase(Unit).successOr(emptyList())
            "manual" -> getActivedManualColumnUseCase(Unit).successOr(emptyList())
            "history" -> getHistoryActivedColumnUseCase(Unit).successOr(emptyList())
            "card" -> emptyList()
            "piechart" -> emptyList()
            else -> throw Error("column type is not found!")
        }
        _activedColumnState.emit(data)
    }

    fun getColumn() {
        viewModelScope.launch {
            val columns = when (_columnType.value?.lowercase()) {
                "market" -> getMarketColumnListUseCase(_activedColumnState.value).successOr(emptyList())
                "manual" -> getManualColumnListUseCase(_activedColumnState.value).successOr(emptyList())
                "history" -> getHistoryColumnUseCase(_activedColumnState.value).successOr(emptyList())
                "card" -> emptyList()
                "piechart" -> emptyList()
                else -> throw Error("column type is not found!")
            }
            _columnState.emit(columns)
        }
    }

}