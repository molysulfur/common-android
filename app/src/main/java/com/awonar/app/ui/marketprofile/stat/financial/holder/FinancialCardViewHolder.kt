package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemFinancialCardBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem

class FinancialCardViewHolder constructor(private val binding: AwonarItemFinancialCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(marketItem: FinancialMarketItem.FinancialCardItem) {
        with(binding.awonarItemCardFinancial) {
            setTitle(marketItem.title ?: "")
            setDate(marketItem.date ?: "")
            setValue(marketItem.valueString ?: "")
        }
    }
}