package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.market.adapter.InstrumentItem

class InstrumentItemViewHolder constructor(private val binding: AwonarItemInstrumentListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: InstrumentItem.InstrumentListItem) {
        binding.awonarInstrumentItemList.setImage(item.instrument.logo ?: "")
        binding.awonarInstrumentItemList.setTitle(item.instrument.symbol ?: "")
    }
}