package com.awonar.app.ui.watchlist.adapter

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.socialtrade.Trader
import kotlinx.parcelize.Parcelize

sealed class WatchlistItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class ColumnItem(
        val column1: String,
        val column2: String,
        val column3: String,
    ) : WatchlistItem(WatchlistType.WATCHLIST_COLUMNS)

    @Parcelize
    data class InstrumentItem(
        val id: String?,
        val instrumentId: Int,
        val title: String?,
        val image: String?,
    ) : WatchlistItem(WatchlistType.WATCHLIST_INSTRUMENT)

    @Parcelize
    data class TraderItem(
        val id: String?,
        val uid: String?,
        val title: String?,
        val subTitle: String?,
        val image: String?,
        val risk: Int,
        val gain: Float,
    ) : WatchlistItem(WatchlistType.WATCHLIST_TRADER)

    @Parcelize
    data class ButtonItem(
        val buttonText: String?,
        val icon: String? = null,
        val iconRes: Int = 0,
    ) : WatchlistItem(WatchlistType.WATCHLIST_BUTTON)

    @Parcelize
    data class EmptyItem(
        val title: String?,
        val description: String?,
        val icon: String? = null,
        val iconRes: Int = 0,
    ) : WatchlistItem(WatchlistType.WATCHLIST_EMPTY)
}