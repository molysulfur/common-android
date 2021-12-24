package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemCopierCardBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class CopierPositionViewHolder constructor(private val binding: AwonarItemCopierCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.CopierPositionCardItem) {
        binding.item = item
    }
}