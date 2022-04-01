package com.awonar.app.ui.profile

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.profile.stat.StatisticAdapter
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.ui.profile.stat.StatisticLayoutManager
import timber.log.Timber

@BindingAdapter("statisticAdapter", "viewModel")
fun setStatisicAdapter(
    recycler: RecyclerView,
    itemList: MutableList<StatisticItem>,
    viewModel: ProfileViewModel,
) {
    if (recycler.adapter == null) {
        val statAdapter = StatisticAdapter().apply {
            onChecked = {
                viewModel.changeTypeGrowth(it)

            }
            onSelected = { yearString ->
                try {
                    yearString?.toInt()?.let {
                        viewModel.getGrowthStatistic("",it)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            onClick = {
                viewModel.toggleShowMoreGrowth()
            }
        }
        with(recycler) {
            adapter = statAdapter
            layoutManager = StatisticLayoutManager(recycler.context, statAdapter, 3)
        }
    }
    (recycler.adapter as StatisticAdapter).itemList = itemList
}