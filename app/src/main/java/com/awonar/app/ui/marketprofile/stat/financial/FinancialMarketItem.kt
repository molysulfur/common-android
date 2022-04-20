package com.awonar.app.ui.marketprofile.stat.financial

import android.os.Parcelable
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.parcelize.Parcelize

sealed class FinancialMarketItem(val type: Int) : Parcelable {

    @Parcelize
    data class ListSelectorItem(
        val key: String?,
        val text: String?,
        val value: Long,
        val color: Int,
    ) : FinancialMarketItem(FinancialMarketType.LIST_SELECTOR)

    @Parcelize
    data class DropdownItem(
        val text: String?,
        val default: String?,
        val selectors: ArrayList<String>,
    ) : FinancialMarketItem(FinancialMarketType.DROPDOWN)


    @Parcelize
    data class BarChartItem(
        val entries: List<BarEntryItem>,
    ) : FinancialMarketItem(FinancialMarketType.BARCHART)

    @Parcelize
    data class ButtonGroupItem(
        val button1: String?,
        val button2: String?,
    ) : FinancialMarketItem(FinancialMarketType.TOGGLE_QUATER_TYPE)

    @Parcelize
    data class TitleMarketItem(
        val title: String?,
    ) : FinancialMarketItem(FinancialMarketType.TITLE)


    @Parcelize
    data class FinancialCardItem(
        val title: String?,
        val date: String?,
        val valueString: String?,
    ) : FinancialMarketItem(FinancialMarketType.INFO_CARD)

    @Parcelize
    class TabsItem : FinancialMarketItem(FinancialMarketType.TABS)

    @Parcelize
    data class BarEntryItem(
        val key: String,
        val title: String,
        val entries: List<BarEntry>,
    ) : Parcelable
}