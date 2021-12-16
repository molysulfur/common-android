package com.awonar.app.ui.payment.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemDividerBlankBinding
import com.awonar.app.databinding.AwonarItemListBinding

class HistoryViewHolder constructor(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onClick: (() -> Unit)?) {
        binding.text = "History"
        binding.awonarButtonItemButtonList.setOnClickListener {
            onClick?.invoke()
        }
    }

}