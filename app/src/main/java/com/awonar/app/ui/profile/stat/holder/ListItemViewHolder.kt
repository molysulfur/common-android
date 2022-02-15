package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class ListItemViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: StatisticItem.ListItem) {
        with(binding.awonarItemListText) {
            setTitle(item.text ?: "")
            setMeta("%.2f %s".format(item.number, "%"))
        }
    }
}