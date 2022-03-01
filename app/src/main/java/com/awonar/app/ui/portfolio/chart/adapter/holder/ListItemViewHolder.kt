package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem

class ListItemViewHolder constructor(
    private val binding: AwonarItemListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PositionChartItem.ListItem) {
        with(binding.awonarItemListText) {
            setTitle(item.title ?: "")
            setMeta(item.meta ?: "")
        }
    }
}