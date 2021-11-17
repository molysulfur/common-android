package com.awonar.app.ui.history.adapter

import android.os.Parcelable
import com.awonar.android.model.history.History
import com.awonar.android.model.history.MarketHistory
import com.awonar.app.ui.history.adapter.HistoryType.LOADMORE_HISTORY
import com.awonar.app.ui.history.adapter.HistoryType.MANUAL_HISTORY
import com.awonar.app.ui.history.adapter.HistoryType.MARKET_HISTORY
import kotlinx.parcelize.Parcelize


sealed class HistoryItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class ManualItem(
        val history: History
    ) : HistoryItem(MANUAL_HISTORY)

    @Parcelize
    data class LoadMoreItem(
        val page: Int
    ) : HistoryItem(LOADMORE_HISTORY)

    @Parcelize
    data class MarketItem(
        val market: MarketHistory
    ) : HistoryItem(MARKET_HISTORY)

}