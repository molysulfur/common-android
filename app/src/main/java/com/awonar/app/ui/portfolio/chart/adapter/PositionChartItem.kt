package com.awonar.app.ui.portfolio.chart.adapter

import android.os.Parcelable
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.adapter.PortfolioType
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_BUTTON
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_LIST
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_LOADING
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_SUBTITLE
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_TITLE
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_VIEW
import com.github.mikephil.charting.data.PieEntry
import kotlinx.parcelize.Parcelize

sealed class PositionChartItem(val type: Int) : Parcelable {

    @Parcelize
    class LoadingItem : PositionChartItem(POSITION_CHART_LOADING)

    @Parcelize
    class ListItem(
        val title: String?,
        val meta: String?,
        val labelColor: Int,
    ) : PositionChartItem(POSITION_CHART_LIST)

    @Parcelize
    class PieChartItem(
        val entries: List<PieEntry>,
    ) : PositionChartItem(POSITION_CHART_VIEW)

    @Parcelize
    class ButtonItem(
        val buttonText: String,
    ) : PositionChartItem(POSITION_CHART_BUTTON)

    @Parcelize
    class TitleItem(
        val title: String,
    ) : PositionChartItem(POSITION_CHART_TITLE)

    @Parcelize
    class SubTitleItem(
        val subTitle: String,
    ) : PositionChartItem(POSITION_CHART_SUBTITLE)

}