package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import timber.log.Timber

class InstrumentPortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: OrderPortfolioItem.InstrumentPortfolioItem,
        columns: List<String>,
        quotes: Array<Quote>
    ) {
        val quote = quotes.find { it.id == item.position.instrumentId }
        quote?.let {
            item.current = if (item.position.isBuy) it.bid else it.ask
        }
        binding.column1 = columns[0]
        binding.column2 = columns[1]
        binding.column3 = columns[2]
        binding.column4 = columns[3]
        binding.quote = quote
        binding.item = item
    }

}