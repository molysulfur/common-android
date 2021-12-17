package com.awonar.app.ui.payment.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemDividerBlankBinding

class BlankViewHolder constructor(private val binding: AwonarItemDividerBlankBinding) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.awonar_color_light_gray
            )
        )
    }
}