package com.awonar.app.ui.portfolio.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.app.domain.portfolio.ConvertCopierToItemUseCase
import com.awonar.app.domain.portfolio.ConvertGroupPositionToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToCardItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.inside.PortFolioInsideCopierFragmentDirections
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private val convertPositionToCardItemUseCase: ConvertPositionToCardItemUseCase,
    private val convertPositionGroupPositionToItemUseCase: ConvertGroupPositionToItemUseCase,
    private var convertCopierToItemUseCase: ConvertCopierToItemUseCase,
) : ViewModel() {

    private val _navigateActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigateActions get() = _navigateActions.receiveAsFlow()

    private val _positionItems: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<PortfolioItem>> get() = _positionItems

    fun convertManual(it: List<Position>) {
        viewModelScope.launch {
            val positionItemResult = convertPositionToItemUseCase(it).successOr(emptyList())
            _positionItems.emit(positionItemResult.toMutableList())
        }
    }

    fun convertMarket(positions: List<Position>, copies: List<Copier>) {
        viewModelScope.launch {
            combine(
                flowOf(convertPositionGroupPositionToItemUseCase(positions).successOr(listOf())),
                flowOf(convertCopierToItemUseCase(copies).successOr(listOf()))
            ) { position, copies ->
                val list = mutableListOf<PortfolioItem>()
                list.addAll(position)
                list.addAll(copies)
                list
            }.collect {
                _positionItems.emit(it)
            }
        }
    }

    fun navigateInstrumentInside(index: Int, type: String) {
        viewModelScope.launch {
            when (type) {
                "instrument" -> _navigateActions.send(PositionFragmentDirections.actionPositionFragmentToPortFolioInsideInstrumentPortfolioFragment(
                    index))
                "copies" -> _navigateActions.send(
                    PositionFragmentDirections.actionPositionFragmentToPortFolioInsideCopierPortfolioFragment(
                        index)
                )
                "copies_instrument" -> _navigateActions.send(
                    PortFolioInsideCopierFragmentDirections.actionPortFolioInsideCopierFragmentToPortFolioInsideInstrumentCopierFragment(
                        index)
                )
            }

        }
    }

    fun convertCard(positions: List<Position>) {
        viewModelScope.launch {
            val positionItemResult =
                convertPositionToCardItemUseCase(positions).successOr(emptyList())
            _positionItems.emit(positionItemResult.toMutableList())
        }
    }
}