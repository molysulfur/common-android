package com.awonar.app.ui.marketprofile.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDescriptionBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.about.holder.DescriptionViewHolder
import com.awonar.app.ui.marketprofile.about.holder.TitleViewHolder

class MarketAboutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<MarketAboutItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MarketAboutType.TITLE -> TitleViewHolder(
                AwonarItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            MarketAboutType.DESCRIPTION -> DescriptionViewHolder(
                AwonarItemDescriptionBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as MarketAboutItem.TitleItem)
            is DescriptionViewHolder -> holder.bind(item as MarketAboutItem.DescriptionItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

}