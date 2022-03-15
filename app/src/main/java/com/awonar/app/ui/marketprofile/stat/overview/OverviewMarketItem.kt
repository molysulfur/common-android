package com.awonar.app.ui.marketprofile.stat.overview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OverviewMarketItem(val type: Int) : Parcelable {

    @Parcelize
    data class TitleMarketItem(
        val title: String?,
    ) : OverviewMarketItem(OverviewMarketType.TITLE)


    @Parcelize
    data class InfoMarketItem(
        val title: String?,
        val description: String?,
    ) : OverviewMarketItem(OverviewMarketType.INFO)

}