package com.awonar.app.ui.marketprofile.stat.financial

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class FinancialMarketItem(val type: Int) : Parcelable {

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

}
