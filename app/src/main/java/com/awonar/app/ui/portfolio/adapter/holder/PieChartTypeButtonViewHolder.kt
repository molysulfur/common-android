package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class PieChartTypeButtonViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.ButtonItem, onButtonClick: ((String) -> Unit)?) {
        binding.buttonText = item.buttonText
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onButtonClick?.invoke(item.buttonText)
        }
    }
}