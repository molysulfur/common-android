package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCopierCardBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class CopierPositionViewHolder constructor(private val binding: AwonarItemCopierCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.CopierPositionCardItem) {
        with(binding.awonarItemCopierCard) {
            val copy = item.copier
            setImage(copy.user.picture ?: "")
            setTitle(copy.user.username ?: "")
        }
        binding.item = item
    }
}