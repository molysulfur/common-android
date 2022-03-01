package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemMarkerListItemBinding
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem

class ListItemViewHolder constructor(
    private val binding: AwonarItemMarkerListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PositionChartItem.ListItem) {
        with(binding.awonarItemMarkerListText) {
            setTitle(item.title ?: "")
            setMeta(item.meta ?: "")
        }
        with(binding.awonarItemMarkerListView) {
            setBackgroundColor(item.labelColor)
        }
    }
}