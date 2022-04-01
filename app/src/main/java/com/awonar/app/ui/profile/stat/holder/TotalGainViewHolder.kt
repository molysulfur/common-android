package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class TotalGainViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: StatisticItem.TotalGainItem) {
        with(binding.awonarItemListText) {
            setTitle(item.title)
            setMeta("%.2f".format(item.gain))
        }
    }

}