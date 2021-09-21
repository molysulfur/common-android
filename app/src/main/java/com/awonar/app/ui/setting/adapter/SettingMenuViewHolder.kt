package com.awonar.app.ui.setting.adapter

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import javax.inject.Inject

class SettingMenuViewHolder @Inject constructor(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(settingItem: SettingItem) {
        when {
            settingItem.buttonText != null -> binding.text = settingItem.buttonText
            settingItem.buttonTextRes > 0 -> binding.text =
                binding.root.context.getString(settingItem.buttonTextRes)
        }


    }
}