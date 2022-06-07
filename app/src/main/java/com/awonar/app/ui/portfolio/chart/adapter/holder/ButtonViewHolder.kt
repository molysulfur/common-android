package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class ButtonViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.ButtonItem, onButtonClick: ((String) -> Unit)?) {
        binding.buttonText = item.buttonText
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onButtonClick?.invoke(item.buttonText)
        }
    }
}