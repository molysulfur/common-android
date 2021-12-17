package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterSubtitleBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class SubTitleViewHolder constructor(private val binding: AwonarItemCenterSubtitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.SubTitleItem) {
        binding.subTitle = item.subTitle
    }
}