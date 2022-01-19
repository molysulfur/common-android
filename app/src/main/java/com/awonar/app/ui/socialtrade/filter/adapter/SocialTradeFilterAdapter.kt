package com.awonar.app.ui.socialtrade.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.socialtrade.filter.adapter.holder.ListTextViewHolder
import com.awonar.app.ui.socialtrade.filter.adapter.holder.SectionViewHolder

class SocialTradeFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SocialTradeFilterItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            SocialTradeFilterType.SECTION_TYPE -> SectionViewHolder(
                AwonarItemSectionBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false),
            )
            SocialTradeFilterType.LIST_TEXT_TYPE -> ListTextViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false),
            )
            else -> throw Error("View type $viewType is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is SectionViewHolder -> holder.bind(item as SocialTradeFilterItem.SectionItem)
            is ListTextViewHolder -> holder.bind(item as SocialTradeFilterItem.TextListItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type
}