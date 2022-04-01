package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemMarkerListItemBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class ListItemViewHolder constructor(private val binding: AwonarItemMarkerListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.ListItem) {
        with(binding.awonarItemMarkerListView) {
            setBackgroundColor(item.color)
        }
        with(binding.awonarItemMarkerListText) {
            setTitle(item.text ?: "")
            setMeta("%.2f %s".format(item.number, "%"))
        }
    }
}