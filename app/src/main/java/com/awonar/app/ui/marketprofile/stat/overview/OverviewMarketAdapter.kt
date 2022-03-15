package com.awonar.app.ui.marketprofile.stat.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInfoBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.stat.overview.holder.InfoViewHolder
import com.awonar.app.ui.marketprofile.stat.overview.holder.TitleViewHolder

class OverviewMarketAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<OverviewMarketItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            OverviewMarketType.TITLE -> TitleViewHolder(
                AwonarItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            OverviewMarketType.INFO -> InfoViewHolder(
                AwonarItemInfoBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as OverviewMarketItem.TitleMarketItem)
            is InfoViewHolder -> holder.bind(item as OverviewMarketItem.InfoMarketItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

}