package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class CopyTradePortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: OrderPortfolioItem.CopierPortfolioItem,
        columns: List<String>,
        quotes: Array<Quote>
    ) {
        val sumFloatingPL = 0f
        item.copier.positions?.forEach { position ->
            val quote = quotes.find { it.id == position.instrumentId }
            quote?.let {
                val current = if (position.isBuy) it.bid else it.ask
                val pl = PortfolioUtil.getProfitOrLoss(
                    current,
                    position.openRate,
                    position.units,
                    item.conversions[position.instrumentId] ?: 1f,
                    position.isBuy
                )
                sumFloatingPL.plus(pl)
            }
        }
        val pl = sumFloatingPL.plus(item.copier.closedPositionsNetProfit)
        val plPercent = pl.div(item.invested)
        val value = pl.plus(item.invested)
        item.profitLoss = pl
        item.profitLossPercent = plPercent
        item.value = value
        binding.column1 = columns[0]
        binding.column2 = columns[1]
        binding.column3 = columns[2]
        binding.column4 = columns[3]
        binding.quote = null
        binding.item = item
    }

}