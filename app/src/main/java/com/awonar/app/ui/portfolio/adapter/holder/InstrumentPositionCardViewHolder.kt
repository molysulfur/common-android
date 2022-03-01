package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class InstrumentPositionCardViewHolder constructor(private val binding: AwonarItemInstrumentPositionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.InstrumentPositionCardItem) {
        binding.item = item
    }
}