package com.awonar.app.ui.portfolio.activedadapter

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding

class ColumnItemListViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String, onClick: ((String) -> Unit)?) {
        binding.text = text
        binding.awonarItemListText.setOnClickListener {
            onClick?.invoke(text)
        }

    }
}