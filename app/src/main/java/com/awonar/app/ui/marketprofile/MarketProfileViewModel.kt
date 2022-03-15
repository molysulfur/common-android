package com.awonar.app.ui.marketprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.shared.domain.marketprofile.GetFinancialInfoUseCase
import com.awonar.android.shared.domain.marketprofile.GetMarketProfileUseCase
import com.awonar.android.shared.domain.marketprofile.GetOverviewMarketProfileUseCase
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketProfileViewModel @Inject constructor(
    private val getMarketProfileUseCase: GetMarketProfileUseCase,
    private val getOverviewMarketProfileUseCase: GetOverviewMarketProfileUseCase,
    private val getFinancialInfoUseCase: GetFinancialInfoUseCase,
) : ViewModel() {

    private val _instrumentState = MutableStateFlow<InstrumentProfile?>(null)
    val instrumentState: StateFlow<InstrumentProfile?> get() = _instrumentState

    val financalState: StateFlow<FinancialResponse?> =
        _instrumentState.transformLatest { instrument ->
            getFinancialInfoUseCase(instrument?.id ?: 0).collect {
                emit(it.successOr(null))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val overviewMarketState: StateFlow<MutableList<OverviewMarketItem>> =
        _instrumentState.transformLatest { instrument ->
            getOverviewMarketProfileUseCase(instrument?.id ?: 0).collect {
                val data = it.successOr(null)
                val itemList = mutableListOf<OverviewMarketItem>()
                itemList.add(OverviewMarketItem.TitleMarketItem("Overview"))
                itemList.add(OverviewMarketItem.InfoMarketItem("Prev Close",
                    "%.2f".format(data?.prevClose)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Day's Range",
                    "%.2f - %.2f".format(data?.dayRange?.low ?: 0f, data?.dayRange?.high ?: 0f)))
                itemList.add(OverviewMarketItem.InfoMarketItem("52 Week Range",
                    "%.2f - %.2f".format(data?.yearRange?.low ?: 0f, data?.yearRange?.high ?: 0f)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Averagge Volume",
                    "%s".format(data?.info?.averageVolume)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Year Return",
                    "%.2f %s".format(data?.yearReturn, "%")))
                itemList.add(OverviewMarketItem.InfoMarketItem("Beta",
                    "%.2f".format(data?.info?.beta ?: 0f)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Market Cap ($)",
                    "%s".format(data?.info?.marketCap ?: 0L)))
                itemList.add(OverviewMarketItem.InfoMarketItem("P/E Ratio",
                    "%.2f".format(data?.info?.peRatio ?: 0f)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Revenue ($)",
                    "%s".format(data?.info?.revenue ?: 0L)))
                itemList.add(OverviewMarketItem.InfoMarketItem("EPS",
                    "%.2f".format(data?.info?.eps ?: 0f)))
                itemList.add(OverviewMarketItem.InfoMarketItem("Divided (Yield)",
                    "%.2f".format(data?.info?.dividend ?: 0f)))
                emit(itemList)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mutableListOf())


    fun getInstrumentProfile(id: Int) {
        viewModelScope.launch {
            getMarketProfileUseCase(id).collect {
                _instrumentState.value = it.successOr(null)
            }
        }
    }


}