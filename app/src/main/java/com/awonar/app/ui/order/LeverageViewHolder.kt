package com.awonar.app.ui.order

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding

class LeverageViewHolder(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String, onClick: ((String) -> Unit)?) {
        binding.text = "X$text"
        binding.awonarButtonItemButtonList.setOnClickListener {
            onClick?.invoke(text)
        }
    }
}