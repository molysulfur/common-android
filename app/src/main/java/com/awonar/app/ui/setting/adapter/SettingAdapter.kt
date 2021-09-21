package com.awonar.app.ui.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding

class SettingAdapter : RecyclerView.Adapter<SettingMenuViewHolder>() {

    var itemLists: List<SettingItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingMenuViewHolder =
        SettingMenuViewHolder(
            AwonarItemButtonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SettingMenuViewHolder, position: Int) {
        holder.bind(itemLists[position])
    }

    override fun getItemCount(): Int = itemLists.size
}