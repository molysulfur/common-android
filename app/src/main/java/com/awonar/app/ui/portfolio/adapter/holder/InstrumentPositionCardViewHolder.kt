package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentPositionBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class InstrumentPositionCardViewHolder constructor(private val binding: AwonarItemInstrumentPositionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.InstrumentPositionCardItem, quote: Quote?) {
        binding.item = item
        quote?.let {
            binding.quote = quote
        }
    }
}