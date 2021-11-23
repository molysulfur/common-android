package com.awonar.app.ui.history.adapter

import android.os.Parcelable
import com.awonar.android.model.history.History
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.user.Master
import com.awonar.app.ui.history.adapter.HistoryType.CASHFLOW_HISTORY
import com.awonar.app.ui.history.adapter.HistoryType.DIVIDER_HISTORY
import com.awonar.app.ui.history.adapter.HistoryType.LOADMORE_HISTORY
import com.awonar.app.ui.history.adapter.HistoryType.POSITION_HISTORY
import kotlinx.parcelize.Parcelize


sealed class HistoryItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class CashFlowItem(
        val logo: Int,
        val title: String?,
        val subTitle: String?,
        val id: String?,
        val amount: Float,
        val status: String?,
        val description: String?,
        val fee: Float,
        val netWithdraw: Float,
        val rate: Float,
        val localAmount: Float
    ) : HistoryItem(CASHFLOW_HISTORY)

    @Parcelize
    data class PositionItem(
        val id: String? = null,
        val invested: Float = 0f,
        val pl: Float = 0f,
        val plPercent: Float = 0f,
        val fee: Float = 0f,
        val endValue: Float = 0f,
        val detail: String? = null,
        val transactionType: Int = 0,
        val picture: String? = null,
        val master: Master? = null,
        val history: History? = null,
        val positionType: String? = null,
        var buy: Float = 0f,
        var sell: Float = 0f
    ) : HistoryItem(POSITION_HISTORY)

    @Parcelize
    data class LoadMoreItem(
        val page: Int
    ) : HistoryItem(LOADMORE_HISTORY)

    @Parcelize
    class DividerItem : HistoryItem(DIVIDER_HISTORY)


}