package com.awonar.app.ui.marketprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.shared.domain.marketprofile.GetFinancialInfoUseCase
import com.awonar.android.shared.domain.marketprofile.GetMarketProfileUseCase
import com.awonar.android.shared.domain.marketprofile.GetOverviewMarketProfileUseCase
import com.awonar.app.domain.marketprofile.*
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
    private val convertFinancialToCardItemUseCase: ConvertFinancialToCardItemUseCase,
    private val convertFinancialStatisticUseCase: ConvertFinancialStatisticUseCase,
    private val convertFinancialIncomeStatementUseCase: ConvertFinancialIncomeStatementUseCase,
    private val convertFinancialBalanceSheetUseCase: ConvertFinancialBalanceSheetUseCase,
    private val convertFinancialCashflowUseCase: ConvertFinancialCashflowUseCase,
) : ViewModel() {
    private val _financialState: MutableStateFlow<FinancialResponse?> = MutableStateFlow(null)
    private val _financialCurrentTab = MutableStateFlow("Statistic")
    private val _quarter = MutableStateFlow("Q1")
    private val _fiscal = MutableStateFlow(Calendar.getInstance().apply {
        add(Calendar.YEAR, -1)
    }.get(Calendar.YEAR).toString())
    private val _quarterType = MutableStateFlow<String?>("annual")

    private val _barEntry =
        MutableStateFlow<MutableList<FinancialMarketItem.BarEntryItem>>(mutableListOf())

    private val _cardItemList = MutableStateFlow(mutableListOf<FinancialMarketItem>())
    val cardItemList get() = _cardItemList
    private val _dropdownList = MutableStateFlow(mutableListOf<FinancialMarketItem>())
    val dropdownList get() = _dropdownList
    private val _financialItemList = MutableStateFlow<MutableList<FinancialMarketItem>>(
        mutableListOf())
    val financialItemList: StateFlow<MutableList<FinancialMarketItem>> get() = _financialItemList

    private val _instrumentState = MutableStateFlow<InstrumentProfile?>(null)
    val instrumentState: StateFlow<InstrumentProfile?> get() = _instrumentState


    init {
        viewModelScope.launch {
            _instrumentState.collectLatest { instrument ->
                getFinancialInfoUseCase(instrument?.id ?: 0).collect {
                    _financialState.emit(it.successOr(null))
                }
            }
        }

        viewModelScope.launch {
            _financialState.collect { financial ->
                if (financial != null) {
                    val cardList =
                        convertFinancialToCardItemUseCase(financial).successOr(mutableListOf())
                    _cardItemList.value = cardList
                    convertFinancialToItem()
                }
            }
        }

        viewModelScope.launch {
            _quarterType.collect { type ->
                val fiscal = _fiscal.value
                val quarter = _quarter.value
                val itemLists = mutableListOf<FinancialMarketItem>()
                if (type == "annual") {
                    val year = fiscal.toInt()
                    itemLists.add(FinancialMarketItem.DropdownItem("Select Year",
                        fiscal,
                        arrayListOf("$year",
                            "${year.minus(1)}",
                            "${year.minus(2)}",
                            "${year.minus(3)}")))
                } else {
                    itemLists.add(FinancialMarketItem.DropdownItem("Select Quarter",
                        quarter,
                        arrayListOf("Q1", "Q2", "Q3", "Q4")))
                }
                _dropdownList.value = itemLists
            }
        }
    }

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
        val financial = _financialState.value
        val quarter = if (_quarterType.value == "annual") "year" else "quarter"
        val barEntries = when (_financialCurrentTab.value) {
            "Income Statement" -> {
                arrayListOf(
                    FinancialMarketItem.BarEntryItem(
                        key = "netIncomeLoss",
                        title = "Net Income/Loss",
                        entries = financial?.incomeStatement?.get(quarter)?.map {
                            BarEntry(0f, it["netIncomeLoss"]?.toFloat() ?: 0f)
                        } ?: emptyList()
                    ),
                    FinancialMarketItem.BarEntryItem(
                        key = "revenues",
                        title = "Revenues",
                        entries = financial?.incomeStatement?.get(quarter)?.map {
                            BarEntry(0f, it["revenues"]?.toFloat() ?: 0f)
                        } ?: emptyList()
                    )
                )
            }
            "Balance Sheet" -> arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    key = "assets",
                    title = "Assets",
                    entries = financial?.balanceSheet?.get(quarter)?.map {
                        BarEntry(0f, it["assets"]?.toFloat() ?: 0f)
                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    key = "liabilities",
                    title = "Liabilities",
                    entries = financial?.balanceSheet?.get(quarter)?.map {
                        BarEntry(0f, it["liabilities"]?.toFloat() ?: 0f)
                    } ?: emptyList()
                )
            )
            "Cashflow" -> arrayListOf(
                FinancialMarketItem.BarEntryItem(
                    key = "netCashFlow",
                    title = "Net Cash Flow",
                    entries = financial?.cashFlow?.get(quarter)?.map {
                        BarEntry(0f, it["netCashFlow"]?.toFloat() ?: 0f)
                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    key = "netCashFlowFromInvestingActivities",
                    title = "Net Cash Flow From Investing Activities",
                    entries = financial?.cashFlow?.get(quarter)?.map {
                        BarEntry(0f, it["netCashFlowFromInvestingActivities"]?.toFloat() ?: 0f)

                    } ?: emptyList()
                ),
                FinancialMarketItem.BarEntryItem(
                    key = "netCashFlowFromOperatingActivitiesContinuing",
                    title = "Net Cash Flow From Operating Activities, Continuing",
                    entries = financial?.cashFlow?.get(quarter)?.map {
                        BarEntry(0f,
                            it["netCashFlowFromOperatingActivitiesContinuing"]?.toFloat() ?: 0f)
                    } ?: emptyList()
                )
            )
            else -> mutableListOf()
        }
        _barEntry.value = barEntries
    }


    fun setQuaterType(quarter: String?) {
        _quarterType.value = quarter
        val oldBarEntries = _barEntry.value
        _barEntry.value = mutableListOf()
        oldBarEntries.forEach {
            addBarEntry(it.key)
        }
        convertFinancialToItem()
    }

    fun setQuater(quarter: String) {
        if (_quarterType.value == "annual") {
            _fiscal.value = quarter
        } else {
            _quarter.value = quarter
        }
        convertFinancialToItem()
    }

    fun setBarEntry(key: String) {
        addBarEntry(key)
        convertFinancialToItem()
    }

    private fun addBarEntry(key: String) {
        val barEntries = _barEntry.value
        val quarter = if (_quarterType.value == "annual") "year" else "quarter"
        val info = when (_financialCurrentTab.value) {
            "Income Statement" -> _financialState.value?.incomeStatement?.get(quarter)
            "Balance Sheet" -> _financialState.value?.balanceSheet?.get(quarter)
            "Cashflow" -> _financialState.value?.cashFlow?.get(quarter)
            else -> null
        }
        val entries: List<BarEntry>? = info?.map {
            BarEntry(barEntries.size.toFloat(), it[key]?.toFloat() ?: 0f)
        }
        val existEntry = barEntries.find { it.key == key }
        if (existEntry == null) {
            barEntries.add(FinancialMarketItem.BarEntryItem(
                key = key,
                title = key,
                entries = entries ?: emptyList()
            ))
            _barEntry.value = barEntries
        } else {
            val newEntry = barEntries.filter { it.key != key }
            _barEntry.value = newEntry.toMutableList()
        }
    }

    private fun convertFinancialToItem() {
        viewModelScope.launch {
            val financial = _financialState.value
            val type = _financialCurrentTab.value
            val itemList: MutableList<FinancialMarketItem> = when (type) {
                "Statistic" -> {
                    convertFinancialStatisticUseCase(ConvertFinancial(
                        financial = financial,
                        current = type,
                        fiscal = _fiscal.value,
                        quarter = _quarter.value,
                        quarterType = _quarterType.value
                    )).successOr(
                        mutableListOf())
                }
                "Income Statement" -> {
                    convertFinancialIncomeStatementUseCase(ConvertFinancial(
                        financial = financial,
                        defaultSet = _barEntry.value,
                        current = type,
                        fiscal = _fiscal.value,
                        quarter = _quarter.value,
                        quarterType = _quarterType.value
                    )).successOr(
                        mutableListOf())
                }
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
            _financialItemList.value = itemList
        }
    }
}

