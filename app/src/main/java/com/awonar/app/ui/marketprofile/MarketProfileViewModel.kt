package com.awonar.app.ui.marketprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.shared.domain.marketprofile.GetFinancialInfoUseCase
import com.awonar.android.shared.domain.marketprofile.GetMarketProfileUseCase
import com.awonar.android.shared.domain.marketprofile.GetOverviewMarketProfileUseCase
import com.awonar.app.domain.marketprofile.ConvertFinancialBalanceSheetUseCase
import com.awonar.app.domain.marketprofile.ConvertFinancialCashflowUseCase
import com.awonar.app.domain.marketprofile.ConvertFinancialIncomeStatementUseCase
import com.awonar.app.domain.marketprofile.ConvertFinancialStatisticUseCase
import com.awonar.app.models.marketprofile.ConvertFinancial
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketItem
import com.github.mikephil.charting.data.BarEntry
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MarketProfileViewModel @Inject constructor(
    private val getMarketProfileUseCase: GetMarketProfileUseCase,
    private val getOverviewMarketProfileUseCase: GetOverviewMarketProfileUseCase,
    private val getFinancialInfoUseCase: GetFinancialInfoUseCase,
    private val convertFinancialStatisticUseCase: ConvertFinancialStatisticUseCase,
    private val convertFinancialIncomeStatementUseCase: ConvertFinancialIncomeStatementUseCase,
    private val convertFinancialBalanceSheetUseCase: ConvertFinancialBalanceSheetUseCase,
    private val convertFinancialCashflowUseCase: ConvertFinancialCashflowUseCase,
) : ViewModel() {

    private val _financialCurrentTab = MutableStateFlow("Statistic")
    private val _quarter = MutableStateFlow("Q1")
    private val _fiscal = MutableStateFlow(Calendar.getInstance().apply {
        add(Calendar.YEAR, -1)
    }.get(Calendar.YEAR).toString())
    private val _quarterType = MutableStateFlow<String?>("annual")
    private val _barEntry =
        MutableStateFlow<MutableList<FinancialMarketItem.BarEntryItem>>(mutableListOf())

    private val _financialItemList = MutableStateFlow<MutableList<FinancialMarketItem>>(
        mutableListOf())
    val financialItemList: StateFlow<MutableList<FinancialMarketItem>> get() = _financialItemList

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

    fun setFinancialTabType(it: String) {
        if (it != _financialCurrentTab.value) {
            _financialCurrentTab.value = it
            setBarEntryByDefault()
            convertFinancialToItem()
        }
    }

    private fun setBarEntryByDefault() {
        val financial = financalState.value

        val barEntries = when (_financialCurrentTab.value) {
            "Income Statement" -> {
                arrayListOf(
                    FinancialMarketItem.BarEntryItem(
                        title = "Net Income/Loss",
                        entries = financial?.incomeStatement?.quarter?.map {
                            BarEntry(it.fiscalYear?.toFloat() ?: 0f, it.netIncomeLoss.toFloat())
                        } ?: emptyList()
                    ),
                    FinancialMarketItem.BarEntryItem(
                        title = "Revenues",
                        entries = financial?.incomeStatement?.quarter?.map {
                            BarEntry(it.fiscalYear?.toFloat() ?: 0f, it.revenues.toFloat())
                        } ?: emptyList()
                    )
                )
            }
            "Balance Sheet" -> arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    title = "Assets",
                    entries = financial?.balanceSheet?.quarter?.map {
                        BarEntry(it.fiscalYear?.toFloat() ?: 0f, it.assets.toFloat())
                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    title = "Liabilities",
                    entries = financial?.balanceSheet?.quarter?.map {
                        BarEntry(it.fiscalYear?.toFloat() ?: 0f, it.liabilities.toFloat())
                    } ?: emptyList()
                )
            )
            "Cashflow" -> arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    title = "Net Cash Flow",
                    entries = financial?.cashFlow?.quarter?.map {
                        BarEntry(it.fiscalYear?.toFloat() ?: 0f, it.netCashFlow.toFloat())
                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    title = "Net Cash Flow From Investing Activities",
                    entries = financial?.cashFlow?.quarter?.map {
                        BarEntry(it.fiscalYear?.toFloat() ?: 0f,
                            it.netCashFlowFromInvestingActivities.toFloat())
                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    title = "Net Cash Flow From Operating Activities, Continuing",
                    entries = financial?.cashFlow?.quarter?.map {
                        BarEntry(it.fiscalYear?.toFloat() ?: 0f,
                            it.netCashFlowFromOperatingActivitiesContinuing.toFloat())
                    } ?: emptyList()
                )
            )
            else -> mutableListOf()
        }
        _barEntry.value = barEntries
    }

    fun convertFinancialToItem() {
        viewModelScope.launch {
            val financial = financalState.value
            val type = _financialCurrentTab.value
            val itemLists = when (type) {
                "Statistic" -> convertFinancialStatisticUseCase(ConvertFinancial(
                    financial = financial,
                    current = type,
                    fiscal = _fiscal.value,
                    quarter = _quarter.value,
                    quarterType = _quarterType.value
                )).successOr(
                    mutableListOf())
                "Income Statement" -> convertFinancialIncomeStatementUseCase(ConvertFinancial(
                    financial = financial,
                    defaultSet = _barEntry.value,
                    current = type,
                    fiscal = _fiscal.value,
                    quarter = _quarter.value,
                    quarterType = _quarterType.value
                )).successOr(
                    mutableListOf())
                "Balance Sheet" -> convertFinancialBalanceSheetUseCase(ConvertFinancial(
                    financial = financial,
                    defaultSet = _barEntry.value,
                    current = type,
                    fiscal = _fiscal.value,
                    quarter = _quarter.value,
                    quarterType = _quarterType.value
                )).successOr(
                    mutableListOf())
                "Cashflow" -> convertFinancialCashflowUseCase(ConvertFinancial(
                    financial = financial,
                    defaultSet = _barEntry.value,
                    current = type,
                    fiscal = _fiscal.value,
                    quarter = _quarter.value,
                    quarterType = _quarterType.value
                )).successOr(mutableListOf())
                else -> mutableListOf()
            }
            _financialItemList.value = itemLists
        }
    }

    fun setBarEntry(entry: FinancialMarketItem.BarEntryItem) {
        val entries = _barEntry.value
        entries.add(entry)
        _barEntry.value = entries
        convertFinancialToItem()
    }

    fun setQuater(quarter: String?) {
        _quarterType.value = quarter
        convertFinancialToItem()
    }
}

