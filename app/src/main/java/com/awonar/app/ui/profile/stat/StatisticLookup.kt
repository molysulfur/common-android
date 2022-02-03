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
        else -> 1
    }
}
