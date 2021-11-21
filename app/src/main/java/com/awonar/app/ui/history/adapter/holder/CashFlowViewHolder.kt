package com.awonar.app.ui.history.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCashflowCollapsibleBinding
import com.awonar.app.ui.history.adapter.HistoryItem

class CashFlowViewHolder constructor(private val binding: AwonarItemCashflowCollapsibleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: HistoryItem.CashFlowItem,
    ) {
        binding.awonarCashFlowCollapsibleItem.setLogo(item.logo)
        binding.awonarCashFlowCollapsibleItem.setTitle("${item.title}")
        binding.awonarCashFlowCollapsibleItem.setSubTitle("${item.subTitle}")
        binding.awonarCashFlowCollapsibleItem.setDescription("${item.description}")
        binding.awonarCashFlowCollapsibleItem.setAmount(item.amount)
        binding.awonarCashFlowCollapsibleItem.setStatus(item.status ?: "")
        binding.awonarCashFlowCollapsibleItem.setFee(item.fee)
        binding.awonarCashFlowCollapsibleItem.setLocalAmount(item.localAmount)
        binding.awonarCashFlowCollapsibleItem.setRate(item.rate)
        binding.awonarCashFlowCollapsibleItem.setNetWithdraw(item.netWithdraw)
        binding.awonarCashFlowCollapsibleItem.setProgress(getProgress(item.status))
    }

    private fun getProgress(status: String?): Int = when (status) {
        "reject" -> 0
        "approve" -> 100
        else -> 0
    }

}