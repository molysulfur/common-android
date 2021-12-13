package com.awonar.app.ui.deposit.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.deposit.adapter.DepositItem

class PaymentListViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: DepositItem.MethodItem, onItemClick: ((String) -> Unit)?) {
        binding.awonarItemListText.setOnClickListener {
            onItemClick?.invoke(item.id)
        }
        binding.text = item.name
    }
}