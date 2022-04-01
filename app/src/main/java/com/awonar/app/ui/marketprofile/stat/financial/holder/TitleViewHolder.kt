package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem

class TitleViewHolder constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(marketItem: FinancialMarketItem.TitleMarketItem) {
        binding.text = marketItem.title
    }
}