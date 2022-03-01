package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterSubtitleBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import timber.log.Timber

class SubTitleViewHolder constructor(private val binding: AwonarItemCenterSubtitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PositionChartItem.SubTitleItem) {
        binding.subTitle = item.subTitle
    }
}