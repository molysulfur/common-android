package com.awonar.app.ui.profile.stat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemBarchartBinding
import com.awonar.app.databinding.AwonarItemButtonGroupBinding
import com.awonar.app.databinding.AwonarItemSelectorBinding
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_BUTTON_GROUP
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_POSITIVE_NEGATIVE
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_CHART_STACKED
import com.awonar.app.ui.profile.stat.StatisticType.STATISTIC_SELECTOR
import com.awonar.app.ui.profile.stat.holder.ButtonGroupViewHolder
import com.awonar.app.ui.profile.stat.holder.PositveNegativeChartViewHolder
import com.awonar.app.ui.profile.stat.holder.SelectorViewHolder

class StatisticAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<StatisticItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            STATISTIC_CHART_POSITIVE_NEGATIVE -> PositveNegativeChartViewHolder(
                AwonarItemBarchartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_CHART_STACKED -> PositveNegativeChartViewHolder(
                AwonarItemBarchartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )

            STATISTIC_BUTTON_GROUP -> ButtonGroupViewHolder(
                AwonarItemButtonGroupBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            STATISTIC_SELECTOR -> SelectorViewHolder(
                AwonarItemSelectorBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("View Type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is ButtonGroupViewHolder -> holder.bind(item as StatisticItem.ButtonGroupItem)
            is SelectorViewHolder -> holder.bind(item as StatisticItem.SelectorItem)
            is PositveNegativeChartViewHolder -> holder.bind(item as StatisticItem.PositiveNegativeChartItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type
}