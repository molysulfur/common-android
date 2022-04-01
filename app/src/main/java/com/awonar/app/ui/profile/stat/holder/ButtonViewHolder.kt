package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class ButtonViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonItem: StatisticItem.ButtonItem, onClick: ((String?) -> Unit)?) {
        binding.buttonText = buttonItem.buttonText
        binding.awonarButtonViewmoreButtonItem.setOnClickListener {
            onClick?.invoke(buttonItem.buttonText)
        }
    }
}