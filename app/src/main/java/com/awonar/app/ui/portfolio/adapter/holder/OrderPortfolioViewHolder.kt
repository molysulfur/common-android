package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class OrderPortfolioViewHolder constructor(
    private val binding: AwonarItemPositionBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: PortfolioItem.InstrumentItem,
        columns: List<String>,
    ) {
//        quote?.let {
//            Timber.e("$it")
//            item.current = if (item.position.isBuy) it.bid else it.ask
//            val pl = PortfolioUtil.getProfitOrLoss(
//                item.current, item.open, item.units, item.conversionRate, item.position.isBuy
//            )
//            val value = PortfolioUtil.getValue(pl, item.invested)
//            val plPercent = PortfolioUtil.profitLossPercent(pl, item.invested)
//            item.profitLoss = pl
//            item.value = value
//            item.profitLossPercent = plPercent
//        }
        binding.awonarInsturmentOrderItem.setOnClickListener {

        }
        if (columns.isNotEmpty()) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
//        binding.item = item
    }

}