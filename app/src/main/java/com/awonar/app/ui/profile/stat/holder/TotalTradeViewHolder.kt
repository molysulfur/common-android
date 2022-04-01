package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTotalTradeBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class TotalTradeViewHolder constructor(private val binding: AwonarItemTotalTradeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(totalTradeItem: StatisticItem.TotalTradeItem) {
        binding.awoanrItemTotalTradeTextTotal.setTitle("%s".format(totalTradeItem.total))
        binding.profit = "%.2f%s".format(totalTradeItem.profitAvg, "%")
        binding.loss = "%.2f%s".format(totalTradeItem.lossAvg, "%")
    }
}