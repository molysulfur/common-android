package com.awonar.app.ui.marketprofile.about

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MarketAboutItem(val type: Int) : Parcelable {

    @Parcelize
    data class TitleItem(
        val title: String?,
    ) : MarketAboutItem(MarketAboutType.TITLE)

    @Parcelize
    data class DescriptionItem(
        val description: String?,
    ) : MarketAboutItem(MarketAboutType.DESCRIPTION)
}