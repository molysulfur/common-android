package com.awonar.app.ui.portfolio.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.app.domain.portfolio.ConvertCopierToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.PortFolioFragmentDirections
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private var convertCopierToItemUseCase: ConvertCopierToItemUseCase,
) : ViewModel() {

    private val _navigateActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigateActions get() = _navigateActions.receiveAsFlow()

    private val _positionItems: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf(OrderPortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionItems

    fun convertManual(it: List<Position>) {
        viewModelScope.launch {
            val positionItemResult = convertPositionToItemUseCase(it).successOr(emptyList())
//            _subscricbeQuote.send(result.successOr(emptyList()).map { it.instrumentId })
            _positionItems.emit(positionItemResult.toMutableList())
        }
    }

    fun convertMarket(positions: List<Position>, copies: List<Copier>) {
        viewModelScope.launch {
            combine(
                flowOf(convertPositionToItemUseCase(positions).successOr(listOf())),
                flowOf(convertCopierToItemUseCase(copies).successOr(listOf()))
            ) { position, copies ->
                val list = mutableListOf<OrderPortfolioItem>()
                list.addAll(position)
                list.addAll(copies)
                list
            }.collect {
                _positionItems.emit(it)
            }
//            _subscricbeQuote.send(result.successOr(emptyList()).map { it.instrumentId })

        }
    }

    fun navigateInstrumentInside(index: Int, type: String) {
        viewModelScope.launch {
            when (type) {
                "instrument" -> _navigateActions.send(PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideInstrumentPortfolioFragment(index))
                "copies" -> _navigateActions.send(
                    PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideCopierPortfolioFragment(index)
                )
            }

        }
    }
}