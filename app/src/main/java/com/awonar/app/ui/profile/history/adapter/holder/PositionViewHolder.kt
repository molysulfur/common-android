package com.awonar.app.ui.profile.history.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem

class PositionViewHolder constructor(private val binding: AwonarItemPositionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: HistoryProfileItem.PositionItem,
        position: Int,
        onClick: ((Int) -> Unit)?,
    ) {
        with(binding.awonarInsturmentOrderItem) {
            setImage(item.image ?: "")
            setTitle(item.symbol ?: "")
            setDescription(item.meta ?: "")
            setOnClickListener {
                onClick?.invoke(position)
            }
            setTextColumnTwo("${item.tradeCount}")
            setTextColumnThree("$%.2f".format(item.profit))
            setTextColumnFour("$%.2f".format(item.pl))
        }
    }
}