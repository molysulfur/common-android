package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortFolioViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val getPositionOrderUseCase: GetPositionOrderUseCase,
    private var getPortfolioActivedColumnPreferenceUseCase: GetPortfolioActivedColumnPreferenceUseCase,
    private var getPortfolioColumnListUseCase: GetPortfolioColumnListUseCase,
    private var updatePortfolioColumnUseCase: UpdatePortfolioColumnUseCase,
    private var resetPortfolioColumnUseCase: ResetPortfolioColumnUseCase
) : ViewModel() {

    private val _navigateActivedColumn = Channel<String>(capacity = Channel.CONFLATED)
    val navigateActivedColumn: Flow<String> = _navigateActivedColumn.receiveAsFlow()

    private val _activedColumnState: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val activedColumnState: StateFlow<List<String>> get() = _activedColumnState

    val columnState: StateFlow<List<String>> = flow {
        val list = getPortfolioColumnListUseCase(Unit).successOr(emptyList())
        emit(list)
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val portfolioState: StateFlow<Portfolio?> = flow {
        getMyPortFolioUseCase(true).collect {
            this.emit(it.successOr(null))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    init {
        viewModelScope.launch {
            val list = getPortfolioActivedColumnPreferenceUseCase(Unit).successOr(emptyList())
            _activedColumnState.emit(list)
        }
    }

    val positionOrderList: StateFlow<List<Position>> = flow {
        getPositionOrderUseCase(Unit).collect {
            emit(it.successOr(emptyList()))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

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
}