package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.app.domain.portfolio.ConvertAllocateToPieChartUseCase
import com.awonar.app.domain.portfolio.ConvertExposureToPieChartUseCase
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioPieChartViewModel @Inject constructor(
    private var getPieChartExposureUseCase: GetPieChartExposureUseCase,
    private var getPieChartAllocateUseCase: GetPieChartAllocateUseCase,
    private var convertExposureToPieChartUseCase: ConvertExposureToPieChartUseCase,
    private var convertAllocateToPieChartUseCase: ConvertAllocateToPieChartUseCase,
    private val getPieChartMarketAllocateUseCase: GetPieChartMarketAllocateUseCase,
    private val getPieChartInstrumentAllocateUseCase: GetPieChartInstrumentAllocateUseCase,
    private val getPieChartInstrumentExposureUseCase: GetPieChartInstrumentExposureUseCase
) : ViewModel() {

    private val _positionOrderList: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf())
    val positionOrderList: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionOrderList


    fun getExposure(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "stocks", "currencies", "crypto" -> getPieChartInstrumentExposureUseCase(Unit)
                else -> getPieChartExposureUseCase(Unit)
            }.collect {
                val data = it.successOr(emptyMap())
                val items = convertExposureToPieChartUseCase(data).successOr(emptyList())
                _positionOrderList.emit(items.toMutableList())
            }
        }
    }

    fun getAllocate(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "market" -> getPieChartMarketAllocateUseCase(Unit)
                "stocks", "currencies", "crypto" -> getPieChartInstrumentAllocateUseCase(type)
                else -> getPieChartAllocateUseCase(Unit)
            }.collect {
                val data = it.successOr(emptyMap())
                val items = convertAllocateToPieChartUseCase(data).successOr(emptyList())
                _positionOrderList.emit(items.toMutableList())
            }
        }
    }

}