package com.awonar.app.ui.socialtrade.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.socialtrade.filter.adapter.holder.ListTextViewHolder
import com.awonar.app.ui.socialtrade.filter.adapter.holder.SectionViewHolder
import com.awonar.app.ui.socialtrade.filter.adapter.holder.SelectorViewHolder

class SocialTradeFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SocialTradeFilterItem> = mutableListOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(SocialTradeFilterDiffCallback(old, value))
                .dispatchUpdatesTo(this)
        }


    var onClick: ((String?) -> Unit)? = null
    var onChecked: ((SocialTradeFilterItem.SelectorListItem) -> Unit)? = null

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
                    false)
            )
            SocialTradeFilterType.LIST_SELECTOR_TYPE -> SelectorViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("View type $viewType is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is SectionViewHolder -> holder.bind(item as SocialTradeFilterItem.SectionItem)
            is ListTextViewHolder -> holder.bind(item as SocialTradeFilterItem.TextListItem,
                onClick)
            is SelectorViewHolder -> holder.bind(item as SocialTradeFilterItem.SelectorListItem,
                onChecked)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

    private class SocialTradeFilterDiffCallback(
        private val oldItems: MutableList<SocialTradeFilterItem>,
        private val newItems: MutableList<SocialTradeFilterItem>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

}