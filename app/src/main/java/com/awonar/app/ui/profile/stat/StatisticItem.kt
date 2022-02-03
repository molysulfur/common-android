package com.awonar.app.ui.profile.stat

import android.os.Parcelable
import com.github.mikephil.charting.data.Entry
import kotlinx.parcelize.Parcelize

sealed class StatisticItem(val type: Int) : Parcelable {

    @Parcelize
    data class ButtonGroupItem(
        val buttonText1: String,
        val buttonText2: String,
    ) : StatisticItem(StatisticType.STATISTIC_BUTTON_GROUP)

    @Parcelize
    data class SelectorItem(
        val selectorList: List<String>,
    ) : StatisticItem(StatisticType.STATISTIC_SELECTOR)

    @Parcelize
    data class PositiveNegativeChartItem(
        val entries: List<Entry>,
    ) : StatisticItem(StatisticType.STATISTIC_CHART_POSITIVE_NEGATIVE)

    @Parcelize
    data class TotalGainItem(
        val entries: List<Entry>,
    ) : StatisticItem(StatisticType.STATISTIC_TOTAL_GAIN)


}