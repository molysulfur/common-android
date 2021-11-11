package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemEmptyBinding

class EmptyViewHolder constructor(
    private val binding: AwonarItemEmptyBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.awonarItemEmptyImageLogo.setImageResource(R.drawable.awonar_ic_chart_searching)
        binding.title = "Portfolio is empty"
        binding.subTitle =
            "Start exploring investment opportunities by copying people and investing in Markets or Copy Portfolio"
    }
}