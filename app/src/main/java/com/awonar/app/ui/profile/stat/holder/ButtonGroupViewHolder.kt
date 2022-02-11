package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemButtonGroupBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class ButtonGroupViewHolder constructor(private val binding: AwonarItemButtonGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonGroupItem: StatisticItem.ButtonGroupItem, onChecked: ((Boolean) -> Unit)?) {
        with(binding.awonarItemButtonGroupContainer) {
            if (buttonGroupItem.isGrowth) {
                check(R.id.awonar_item_button_group_button_second)
            } else {
                check(R.id.awonar_item_button_group_button_first)
            }
            binding.awonarItemButtonGroupButtonFirst.setOnClickListener {
                onChecked?.invoke(false)
            }
            binding.awonarItemButtonGroupButtonSecond.setOnClickListener {
                onChecked?.invoke(true)
            }
        }
        binding.button1 = buttonGroupItem.buttonText1
        binding.button2 = buttonGroupItem.buttonText2
    }
}