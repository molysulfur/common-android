package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemEmptyBinding
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class ExposureViewHolder constructor(
    private val binding: AwonarItemListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.PieChartExposureItem) {
        binding.text = "${item.name} ${item.exposure}"
    }
}