package com.awonar.app.ui.profile

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.profile.stat.StatisticAdapter
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.ui.profile.stat.StatisticLayoutManager
import timber.log.Timber

@BindingAdapter("statisticAdapter")
fun setStatisicAdapter(
    recycler: RecyclerView,
    itemList: MutableList<StatisticItem>,
) {
    if (recycler.adapter == null) {
        val statAdapter = StatisticAdapter()
        with(recycler) {
            adapter = statAdapter
            layoutManager = StatisticLayoutManager(recycler.context, statAdapter, 3)
        }
    }
    (recycler.adapter as StatisticAdapter).itemList = itemList
}