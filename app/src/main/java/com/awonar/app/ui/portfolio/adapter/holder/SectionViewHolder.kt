package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterSubtitleBinding
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import timber.log.Timber

class SectionViewHolder constructor(private val binding: AwonarItemSectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.SectionItem) {
        binding.text = item.text
    }
}