package com.awonar.app.ui.socialtrade.adapter

import android.os.Parcelable
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType
import kotlinx.parcelize.Parcelize

sealed class SocialTradeItem(
    val type: Int,
) : Parcelable {


    @Parcelize
    class TitleItem(
        val title: String
    ) : SocialTradeItem(SocialTradeType.SOCIALTRADE_TITLE)

    @Parcelize
    class CopiesItem(
        val userId :String?,
        val image: String?,
        val title: String?,
        val subTitle: String?,
        val isWatchList: Boolean,
        val gain: Float,
        val risk: Int,
    ) : SocialTradeItem(SocialTradeType.SOCIALTRADE_COPIES_ITEM)

}