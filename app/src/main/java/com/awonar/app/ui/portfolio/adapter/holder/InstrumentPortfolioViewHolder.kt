package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class InstrumentPortfolioViewHolder constructor(private val binding: AwonarItemInstrumentOrderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.InstrumentPortfolioItem) {
        binding.position = item.position
    }

}