package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentPositionBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import timber.log.Timber

class InstrumentPositionCardViewHolder constructor(private val binding: AwonarItemInstrumentPositionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.InstrumentPositionCardItem, quotes: Array<Quote>?) {
        binding.item = item
        quotes?.let {
            val quote = it.find { it.id == item.position.instrumentId }
            binding.quote = quote
        }
    }
}