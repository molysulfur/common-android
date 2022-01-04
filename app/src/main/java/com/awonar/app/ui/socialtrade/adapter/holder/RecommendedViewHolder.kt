package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.socialtrade.CopierRecommended
import com.awonar.app.databinding.AwonarItemCopiesRecommendedBinding

class RecommendedViewHolder constructor(
    private val binding: AwonarItemCopiesRecommendedBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(copierRecommended: CopierRecommended) {
        with(binding.awonarItemCopiesCardHolder) {
            title = copierRecommended.username
            subTitle = copierRecommended.username
            change = copierRecommended.gain
            description = copierRecommended.aumDescription
            risk = copierRecommended.risk
        }
    }
}