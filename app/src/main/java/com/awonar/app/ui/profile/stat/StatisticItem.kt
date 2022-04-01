package com.awonar.app.ui.profile.stat

import android.os.Parcelable
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_LINE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_PIE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_DIVIDER
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_LINEAR_COLORS
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_LIST_ITEM
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_SECTION
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_TITLE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_TOTAL_TRADE
import com.awonar.app.widget.StackedRechartWebView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.parcelize.Parcelize

sealed class StatisticItem(val type: Int) : Parcelable {

    @Parcelize
    data class TitleItem(
        val text: String,
    ) : StatisticItem(STATISTIC_TITLE)

    @Parcelize
    data class SectionItem(
        val text: String,
    ) : StatisticItem(STATISTIC_SECTION)

    @Parcelize
    data class TotalTradeItem(
        val total: Int,
        val profitAvg: Float,
        val lossAvg: Float,
    ) : StatisticItem(STATISTIC_TOTAL_TRADE)

    @Parcelize
    data class LinearColorsItem(
        val weightList: List<Float>,
    ) : StatisticItem(STATISTIC_LINEAR_COLORS)

    @Parcelize
    class DividerItem : StatisticItem(STATISTIC_DIVIDER)

    @Parcelize
    data class ListItem(
        val text: String?,
        val number: Float,
        val color: Int = 0,
    ) : StatisticItem(STATISTIC_LIST_ITEM)

    @Parcelize
    data class PieChartItem(
        val entries: List<PieEntry>,
    ) : StatisticItem(STATISTIC_CHART_PIE)

    @Parcelize
    data class GrowthDayItem(
        val entries: List<Entry>,
    ) : StatisticItem(STATISTIC_CHART_LINE)

    @Parcelize
    data class TextBoxItem(
        val title: String?,
        val subTitle: String?,
    ) : StatisticItem(StatisticType.STATISTIC_TEXT_BOX)

    @Parcelize
    data class StackedChartItem(
        val entries: List<StackedRechartWebView.StackedRechartEntity>,
    ) : StatisticItem(StatisticType.STATISTIC_CHART_STACKED)

    @Parcelize
    data class RiskItem(
        val risk: Int,
    ) : StatisticItem(StatisticType.STATISTIC_RISK)

    @Parcelize
    data class ButtonItem(
        val buttonText: String,
    ) : StatisticItem(StatisticType.STATISTIC_BUTTON)

    @Parcelize
    class BlankItem : StatisticItem(StatisticType.STATISTIC_BLANK)

    @Parcelize
    data class ButtonGroupItem(
        val buttonText1: String,
        val buttonText2: String,
        val isGrowth: Boolean = false,
    ) : StatisticItem(StatisticType.STATISTIC_BUTTON_GROUP)

    @Parcelize
    data class SelectorItem(
        val current: Int,
        val selectorList: List<String>,
    ) : StatisticItem(StatisticType.STATISTIC_SELECTOR)

    @Parcelize
    data class PositiveNegativeChartItem(
        val entries: List<Entry>,
    ) : StatisticItem(StatisticType.STATISTIC_CHART_POSITIVE_NEGATIVE)

    @Parcelize
    data class TotalGainItem(
        val title: String,
        val gain: Float,
    ) : StatisticItem(StatisticType.STATISTIC_TOTAL_GAIN)


}