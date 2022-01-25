package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.socialtrade.Trader
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemCopiesRecommendedBinding
import timber.log.Timber

class RecommendedViewHolder constructor(
    private val binding: AwonarItemCopiesRecommendedBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(trader: Trader, onItemClick: ((String?) -> Unit)?) {
        with(binding.awonarItemCopiesCardHolder) {
            title = if (trader.displayFullName) {
                "${trader.firstName} ${trader.middleName} ${trader.lastName}"
            } else {
                trader.username
            }
            image = trader.image
            subTitle = trader.username
            change = trader.gain
            description = "Return (Last 1Y)"
            risk = trader.risk
            gainColor = when {
                trader.gain > 0 -> R.color.awonar_color_green
                trader.gain < 0 -> R.color.awonar_color_orange
                else -> R.color.awonar_color_gray
            }

            setOnClickListener {
                onItemClick?.invoke(trader.id)
            }
        }
    }
}