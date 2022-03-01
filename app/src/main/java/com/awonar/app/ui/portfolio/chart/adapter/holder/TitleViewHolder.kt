package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterTitleBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem

class TitleViewHolder constructor(private val binding: AwonarItemCenterTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PositionChartItem.TitleItem) {
        binding.title = item.title
    }
}