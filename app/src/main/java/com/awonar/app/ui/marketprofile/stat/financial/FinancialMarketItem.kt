package com.awonar.app.ui.marketprofile.stat.financial

import android.os.Parcelable
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.parcelize.Parcelize

sealed class FinancialMarketItem(val type: Int) : Parcelable {

    @Parcelize
    data class BarChartItem(
        val entries: List<BarEntryItem>,
    ) : FinancialMarketItem(FinancialMarketType.BARCHART)

    @Parcelize
    data class ButtonGroupItem(
        val button1: String?,
        val button2: String?,
        val default: String?,
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
    data class ViewPagerItem(
        val financialType: String?,
    ) : FinancialMarketItem(FinancialMarketType.CATEGORY_PAGER)

    @Parcelize
    data class BarEntryItem(
        val title: String,
        val entries: List<BarEntry>,
    ) : Parcelable
}
