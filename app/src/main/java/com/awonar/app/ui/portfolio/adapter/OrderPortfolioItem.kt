package com.awonar.app.ui.portfolio.adapter

import android.os.Parcelable
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.COPYTRADE_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.INSTRUMENT_PORTFOLIO
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColumnValue(
    var value: Float,
    var type: ColumnValueType,
) : Parcelable

enum class ColumnValueType {
    COLUMN_TEXT,
    COLUMN_BUTTON,
    COLUMN_CURRENT,
    COLUMN_PIP_CHANGE,
    COLUMN_VALUE,
    PROFITLOSS,
    PROFITLOSS_PERCENT
}

sealed class OrderPortfolioItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    class InstrumentPortfolioItem(
        val position: Position,
        val conversionRate :Float,
        var invested: Float,
        var units: Float,
        var open: Float,
        var current: Float,
        var stopLoss: Float,
        var takeProfit: Float,
        var profitLoss: Float,
        var profitLossPercent: Float,
        var pipChange: Float,
        var leverage: Int,
        var value: Float,
        var fees: Float,
        var amountStopLoss: Float,
        var amountTakeProfit: Float,
        var stopLossPercent: Float,
        var takeProfitPercent: Float,
    ) : OrderPortfolioItem(INSTRUMENT_PORTFOLIO)

    @Parcelize
    class CopierPortfolioItem(
        val copier: Copier,
        var units: Float,
        var avgOpen: Float,
        var invested: Float,
        var profitLoss: Float,
        var profitLossPercent: Float,
        var value: Float,
        var fees: Float,
        var leverage: Float,
        var current: Float,
        var netInvested: Float,
        var copyStopLoss: Float,
        var copyStopLossPercent: Float,
    ) : OrderPortfolioItem(COPYTRADE_PORTFOLIO)
}