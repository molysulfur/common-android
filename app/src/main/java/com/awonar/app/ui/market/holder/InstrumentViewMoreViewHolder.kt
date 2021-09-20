package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.market.adapter.InstrumentItem

class InstrumentViewMoreViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InstrumentItem.InstrumentViewMoreItem, onViewMoreClick: ((String) -> Unit)?) {
        binding.buttonText = "View More"
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onViewMoreClick?.invoke(item.viewMoreType)
        }
    }
}