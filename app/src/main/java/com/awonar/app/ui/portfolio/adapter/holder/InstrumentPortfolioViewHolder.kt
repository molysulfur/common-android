package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class InstrumentPortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.InstrumentPortfolioItem, quote: Array<Quote>) {
        val quote = quote.find { it.id == item.position.instrumentId }
        binding.quote = quote
        binding.item = item
    }

}