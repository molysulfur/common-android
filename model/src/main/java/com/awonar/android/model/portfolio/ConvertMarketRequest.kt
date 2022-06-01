package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.market.Quote
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConvertMarketRequest(
    val portfolio: UserPortfolioResponse
//    val quotes: MutableMap<Int, Quote>
) : Parcelable