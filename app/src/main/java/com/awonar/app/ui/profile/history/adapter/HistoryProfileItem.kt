package com.awonar.app.ui.profile.history.adapter

import android.os.Parcelable
import com.awonar.app.ui.profile.history.adapter.HistoryProfileType.LOADMORE
import com.awonar.app.ui.profile.history.adapter.HistoryProfileType.POSITION_HISTORY_PROFILE
import kotlinx.parcelize.Parcelize

sealed class HistoryProfileItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class LoadMoreItem(
        val page: Int,
    ) : HistoryProfileItem(LOADMORE)

    @Parcelize
    data class PositionItem(
        val image: String?,
        val symbol: String?,
        var meta: String?,
        val tradeCount: Int,
        val profit: Float,
        val pl: Float,
    ) : HistoryProfileItem(POSITION_HISTORY_PROFILE)

}