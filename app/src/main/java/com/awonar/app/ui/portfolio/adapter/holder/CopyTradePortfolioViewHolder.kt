package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class CopyTradePortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.CopierPortfolioItem, quote: Array<Quote>) {
//        binding.quote = quote
        binding.item = item
    }

}