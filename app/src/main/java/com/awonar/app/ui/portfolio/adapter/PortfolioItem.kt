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
    class ViewAllItem(
        val text: String,
    ) : PortfolioItem(VIEWALL_BUTTON)

    @Parcelize
    class PieChartItem(
        val entries: List<PieEntry>,
    ) : PortfolioItem(PIECHART_PORTFOLIO)

    @Parcelize
    class ButtonItem(
        val buttonText: String,
    ) : PortfolioItem(BUTTON_PORTFOLIO)

    @Parcelize
    class TitleItem(
        val title: String,
    ) : PortfolioItem(TITLE_CENTER_PORTFOLIO)

    @Parcelize
    class SubTitleItem(
        val subTitle: String,
    ) : PortfolioItem(SUBTITLE_CENTER_PORTFOLIO)

    @Parcelize
    class SectionItem(
        val text: String,
    ) : PortfolioItem(SECTION_PORTFOLIO)

    @Parcelize
    class EmptyItem : PortfolioItem(EMPTY_PORTFOLIO)

    @Parcelize
    class InstrumentPortfolioItem(
        val position: Position,
        var date: String?,
        val index: Int,
        val isRealTime: Boolean = false,
    ) : PortfolioItem(INSTRUMENT_PORTFOLIO)

    @Parcelize
    class CopierPortfolioItem(
        val copier: Copier,
        val conversions: Map<Int, Float>,
        var index: Int,
    ) : PortfolioItem(COPYTRADE_PORTFOLIO)

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