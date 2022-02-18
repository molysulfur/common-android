package com.awonar.app.ui.profile.stat

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class StatisticLookup(private val adapter: RecyclerView.Adapter<*>, private val columnCount: Int) :
    GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int = when (adapter.getItemViewType(position)) {
        StatisticType.STATISTIC_BUTTON_GROUP -> columnCount
        StatisticType.STATISTIC_SELECTOR -> columnCount
        StatisticType.STATISTIC_CHART_POSITIVE_NEGATIVE -> columnCount
        StatisticType.STATISTIC_TOTAL_GAIN -> columnCount
        StatisticType.STATISTIC_BLANK -> columnCount
        StatisticType.STATISTIC_BUTTON -> columnCount
        StatisticType.STATISTIC_RISK -> columnCount
        StatisticType.STATISTIC_CHART_STACKED -> columnCount
        StatisticType.STATISTIC_CHART_LINE -> columnCount
        StatisticType.STATISTIC_CHART_PIE -> columnCount
        StatisticType.STATISTIC_LIST_ITEM -> columnCount
        StatisticType.STATISTIC_DIVIDER -> columnCount
        StatisticType.STATISTIC_TOTAL_TRADE -> columnCount
        StatisticType.STATISTIC_LINEAR_COLORS -> columnCount
        else -> 1
    }
}
