package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding

class TitleViewHolder constructor(
    private val binding: AwonarItemTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.text = text
    }
}