package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterTitleBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class TitleViewHolder constructor(private val binding: AwonarItemCenterTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.TitleItem) {
        binding.title = item.title
    }
}