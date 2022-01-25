package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemCopiesItemBinding
import com.awonar.app.ui.socialtrade.adapter.SocialTradeItem

class CopiesItemViewHolder constructor(
    private val binding: AwonarItemCopiesItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        trader: SocialTradeItem.CopiesItem,
        onItemClick: ((String?) -> Unit)?,
        onWatchListClick: (() -> Unit)?
    ) {
        with(binding.awonarItemCopiesItemHolder) {
            setOnClickListener {
                onItemClick?.invoke(trader.userId)
            }
            title = trader.title
            image = trader.image
            subTitle = trader.subTitle
            gain = trader.gain
            risk = trader.risk
            gainColor = when {
                trader.gain > 0 -> R.color.awonar_color_green
                trader.gain < 0 -> R.color.awonar_color_orange
                else -> R.color.awonar_color_gray
            }
        }
    }
}