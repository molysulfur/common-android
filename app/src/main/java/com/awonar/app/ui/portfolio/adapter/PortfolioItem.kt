package com.awonar.app.ui.portfolio.adapter

import android.os.Parcelable
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.portfolio.Position
import com.awonar.app.ui.portfolio.adapter.PortfolioType.BALANCE_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.BUTTON_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.COPYTRADE_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.COPY_POSITION_CARD
import com.awonar.app.ui.portfolio.adapter.PortfolioType.EMPTY_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.INSTRUMENT_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.INSTRUMENT_POSITION_CARD
import com.awonar.app.ui.portfolio.adapter.PortfolioType.LIST_ITEM_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.ORDER_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.PIECHART_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.SECTION_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.SUBTITLE_CENTER_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.TITLE_CENTER_PORTFOLIO
import com.awonar.app.ui.portfolio.adapter.PortfolioType.VIEWALL_BUTTON
import com.github.mikephil.charting.data.PieEntry
import kotlinx.parcelize.Parcelize

sealed class PortfolioItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class BalanceItem(
        val title: String?,
        val value: Float,
    ) : PortfolioItem(BALANCE_PORTFOLIO)

    @Parcelize
    class SectionItem(
        val text: String,
    ) : PortfolioItem(SECTION_PORTFOLIO)

    @Parcelize
    class EmptyItem : PortfolioItem(EMPTY_PORTFOLIO)

    @Parcelize
    class PositionItem(
        val positionId: Int,
        val instrumentGroup: List<Position>? = null,
        val conversionRate: Float = 0f,
        var invested: Float = 0f,
        var current: Float = 0f,
        var units: Float = 0f,
        var openRate: Float = 0f,
        var pl: Float = 0f,
        var plPercent: Float = 0f,
        var pipChange: Int = 0,
        var leverage: Float = 0f,
        var value: Float = 0f,
        var fees: Float = 0f,
        var stopLoss: Float = 0f,
        var takeProfit: Float = 0f,
        var amountStopLoss: Float = 0f,
        var amountTakeProfit: Float = 0f,
        var stopLossPercent: Float = 0f,
        var takeProfitPercent: Float = 0f,
        var buyOrSell: Float = 0f
    ) : PortfolioItem(INSTRUMENT_PORTFOLIO)

    @Parcelize
    class CopierPortfolioItem(
        var copier: Copier,
        val conversions: Map<Int, Float>,
        var index: Int,
    ) : PortfolioItem(COPYTRADE_PORTFOLIO)

    @Parcelize
    class InstrumentPositionCardItem(
        val position: Position,
        val conversion: Float,
    ) : PortfolioItem(INSTRUMENT_POSITION_CARD)

    @Parcelize
    class CopierPositionCardItem(
        val copier: Copier,
        var invested: Float,
        var money: Float,
        var value: Float,
        var profitLoss: Float,
    ) : PortfolioItem(COPY_POSITION_CARD)

    @Parcelize
    class ListItem(
        val name: String,
        val value: Float,
    ) : PortfolioItem(LIST_ITEM_PORTFOLIO)

    @Parcelize
    class InstrumentItem(
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
    ) : PortfolioItem(ORDER_PORTFOLIO)
}