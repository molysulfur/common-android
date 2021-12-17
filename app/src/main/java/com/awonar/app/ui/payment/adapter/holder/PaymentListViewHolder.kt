package com.awonar.app.ui.payment.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.payment.adapter.DepositItem

class PaymentListViewHolder constructor(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: DepositItem.MethodItem, onItemClick: ((String) -> Unit)?) {
        binding.awonarButtonItemButtonList.setOnClickListener {
            onItemClick?.invoke(item.id)
        }
        binding.text = item.name
    }
}