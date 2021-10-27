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
    open val value1: ColumnValue? = null,
    open val value2: ColumnValue? = null,
    open val value3: ColumnValue? = null,
    open val value4: ColumnValue? = null
) : Parcelable {

    @Parcelize
    class InstrumentPortfolioItem(
        val position: Position,
        val invested: Float,
        val units: Float,
        val open: Float,
        val current: Float,
        val stopLoss: Float,
        val takeProfit: Float,
        val profitLoss: Float,
        val profitLossPercent: Float,
        val pipChange: Float,
        val leverage: Int,
        val value: Float,
        val fees: Float,
        val amountStopLoss: Float,
        val amountTakeProfit: Float,
        val stopLossPercent: Float,
        val takeProfitPercent: Float,
    ) : OrderPortfolioItem(INSTRUMENT_PORTFOLIO)

    @Parcelize
    class CopierPortfolioItem(
        val copier: Copier,
        val units: Float,
        val avgOpen: Float,
        val invested: Float,
        val profitLoss: Float,
        val profitLossPercent: Float,
        val value: Float,
        val fees: Float,
        val leverage: Float,
        val current: Float,
        val netInvested: Float,
        val copyStopLoss: Float,
        val copyStopLossPercent: Float,
    ) : OrderPortfolioItem(COPYTRADE_PORTFOLIO)
}