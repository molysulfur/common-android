package com.awonar.app.ui.marketprofile.about.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDescriptionBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.about.MarketAboutItem

class DescriptionViewHolder constructor(private val binding: AwonarItemDescriptionBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MarketAboutItem.DescriptionItem){
            binding.text = item.description
        }
}