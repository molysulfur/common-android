package com.awonar.app.ui.portfolio.adapter

import android.os.Parcelable
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.OrderPortfolio
import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.BUTTON_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.COPYTRADE_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.COPY_POSITION_CARD
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.EMPTY_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.INSTRUMENT_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.INSTRUMENT_POSITION_CARD
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.LIST_ITEM_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.ORDER_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.PIECHART_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.SUBTITLE_CENTER_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioType.TITLE_CENTER_PORTFOLIO
import com.github.mikephil.charting.data.PieEntry
import kotlinx.parcelize.Parcelize

sealed class OrderPortfolioItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    class PieChartItem(
        val entries: List<PieEntry>
    ) : OrderPortfolioItem(PIECHART_PORTFOLIO)

    @Parcelize
    class ButtonItem(
        val buttonText: String
    ) : OrderPortfolioItem(BUTTON_PORTFOLIO)

    @Parcelize
    class TitleItem(
        val title: String
    ) : OrderPortfolioItem(TITLE_CENTER_PORTFOLIO)

    @Parcelize
    class SubTitleItem(
        val subTitle: String
    ) : OrderPortfolioItem(SUBTITLE_CENTER_PORTFOLIO)

    @Parcelize
    class EmptyItem : OrderPortfolioItem(EMPTY_PORTFOLIO)

    @Parcelize
    class InstrumentPortfolioItem(
        val position: Position,
        val conversionRate: Float,
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
        val conversions: Map<Int, Float>,
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

    @Parcelize
    class InstrumentPositionCardItem(
        val position: Position,
        val conversion: Float,
        var units: Float,
        var avgOpen: Float,
        var invested: Float,
        var profitLoss: Float,
        var value: Float,
        var leverage: Float,
        var current: Float,
    ) : OrderPortfolioItem(INSTRUMENT_POSITION_CARD)

    @Parcelize
    class CopierPositionCardItem(
        val copier: Copier,
        var invested: Float,
        var money: Float,
        var value: Float,
        var profitLoss: Float
    ) : OrderPortfolioItem(COPY_POSITION_CARD)

    @Parcelize
    class ListItem(
        val name: String,
        val value: Float
    ) : OrderPortfolioItem(LIST_ITEM_PORTFOLIO)

    @Parcelize
    class InstrumentOrderItem(
        val position: PendingOrder,
        val conversionRate: Float,
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
    ) : OrderPortfolioItem(ORDER_PORTFOLIO)
}