package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.market.adapter.InstrumentItem

class InstrumentViewMoreViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: InstrumentItem.InstrumentViewMoreItem) {
        binding.buttonText = "View More"
    }
}