package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonGroupBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class ButtonGroupViewHolder constructor(private val binding: AwonarItemButtonGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(buttonGroupItem: StatisticItem.ButtonGroupItem) {
        binding.button1 = buttonGroupItem.buttonText1
        binding.button2 = buttonGroupItem.buttonText2
    }
}