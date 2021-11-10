package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import timber.log.Timber

class OrderPortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: OrderPortfolioItem.InstrumentOrderItem,
        columns: List<String>,
        quotes: Array<Quote>,
        onClick: ((String, String) -> Unit)?
    ) {
        Timber.e("$item")
        val quote = quotes.find { it.id == item.position.instrumentId }
        quote?.let {
            item.current = if (item.position.isBuy) it.bid else it.ask
            val pl = PortfolioUtil.getProfitOrLoss(
                item.current, item.open, item.units, item.conversionRate, item.position.isBuy
            )
            val value = PortfolioUtil.getValue(pl, item.invested)
            val plPercent = PortfolioUtil.profitLossPercent(pl, item.invested)
            item.profitLoss = pl
            item.value = value
            item.profitLossPercent = plPercent
        }
        binding.awonarInsturmentOrderItem.setOnClickListener {
            item.position.let {
                onClick?.invoke("${it.instrumentId}", "instrument")
            }
        }
        if (columns.isNotEmpty()) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
        binding.quote = quote
        binding.item = item
    }

}