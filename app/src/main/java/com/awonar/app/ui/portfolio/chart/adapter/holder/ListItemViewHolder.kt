package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemMarkerListItemBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class ListItemViewHolder constructor(
    private val binding: AwonarItemMarkerListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.ListTextItem) {
        with(binding.awonarItemMarkerListText) {
            setTitle(item.title ?: "")
            setMeta(item.meta ?: "")
        }
        with(binding.awonarItemMarkerListView) {
            setBackgroundColor(item.labelColor)
        }
    }
}