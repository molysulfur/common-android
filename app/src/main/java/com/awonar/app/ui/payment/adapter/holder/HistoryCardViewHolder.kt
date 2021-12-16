package com.awonar.app.ui.payment.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPaymentHistoryBinding
import com.awonar.app.ui.payment.adapter.DepositItem
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.DateUtils

class HistoryCardViewHolder constructor(private val binding: AwonarItemPaymentHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DepositItem.HistoryItem) {
        binding.paymentNo = item.id
        binding.date = DateUtils.getDate(item.date)
        binding.description = item.description
        binding.status = item.status
        binding.amount = "$%.2f".format(item.amount)
        binding.awonarItemPaymentHistoryTextAmount.setTextColor(
            ColorChangingUtil.getTextColorChange(
                binding.root.context,
                item.amount
            )
        )
    }

}