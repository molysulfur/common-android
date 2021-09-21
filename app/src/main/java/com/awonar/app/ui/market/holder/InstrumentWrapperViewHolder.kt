package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemRecyclerBinding
import com.awonar.app.ui.market.adapter.InstrumentHorizontalAdapter

class InstrumentWrapperViewHolder constructor(private val binding: AwonarItemRecyclerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(adapter: InstrumentHorizontalAdapter) {
        val context = binding.root.context
        binding.awonarItemRecyclerContainer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.awonarItemRecyclerContainer.adapter = adapter
    }
}