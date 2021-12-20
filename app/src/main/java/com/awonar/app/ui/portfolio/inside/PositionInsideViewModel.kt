package com.awonar.app.ui.portfolio.inside

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionInsideViewModel @Inject constructor(
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
) : ViewModel() {

    private val _positionState: MutableStateFlow<Position?> = MutableStateFlow(null)
    val positionState: StateFlow<Position?> get() = _positionState

    private val _positionItems: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf(OrderPortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionItems

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
}