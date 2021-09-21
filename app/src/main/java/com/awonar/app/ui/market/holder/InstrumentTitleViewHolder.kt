package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.market.adapter.InstrumentItem

class InstrumentTitleViewHolder constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: InstrumentItem.TitleItem) {
        binding.text = item.title
    }
}