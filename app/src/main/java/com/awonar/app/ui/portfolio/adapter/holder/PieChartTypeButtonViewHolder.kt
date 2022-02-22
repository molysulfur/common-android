package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class PieChartTypeButtonViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.ButtonItem, onButtonClick: ((String) -> Unit)?) {
        binding.buttonText = item.buttonText
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onButtonClick?.invoke(item.buttonText)
        }
    }
}