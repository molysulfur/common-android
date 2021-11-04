package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class ButtonViewHolder constructor(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.ButtonItem, onButtonClick: ((String) -> Unit)?) {
        binding.text = item.buttonText
        binding.awonarButtonItemButtonList.setOnClickListener {
            onButtonClick?.invoke(item.buttonText)
        }
    }
}