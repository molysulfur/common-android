package com.awonar.app.ui.profile.stat

import android.os.Parcelable
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.parcelize.Parcelize

sealed class StatisticItem(val type: Int) : Parcelable {

    @Parcelize
    data class TextBoxItem(
        val title: String?,
        val subTitle: String?,
    ) : StatisticItem(StatisticType.STATISTIC_TEXT_BOX)

    @Parcelize
    data class StackedChartItem(
        val entries: List<BarEntry>,
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