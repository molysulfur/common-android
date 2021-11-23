package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class ViewAllViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.ViewAllItem, onButtonClick: (() -> Unit)?) {
        binding.buttonText = item.text
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onButtonClick?.invoke()
        }
    }
}