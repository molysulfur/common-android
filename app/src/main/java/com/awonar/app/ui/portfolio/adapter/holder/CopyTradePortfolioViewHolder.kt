package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class CopyTradePortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: OrderPortfolioItem.CopierPortfolioItem,
        columns: List<String>,
        quote: Array<Quote>
    ) {
        binding.column1 = columns[0]
        binding.column2 = columns[1]
        binding.column3 = columns[2]
        binding.column4 = columns[3]
        binding.quote = null
        binding.item = item
    }

}