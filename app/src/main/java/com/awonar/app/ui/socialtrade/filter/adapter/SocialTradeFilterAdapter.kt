package com.awonar.app.ui.socialtrade.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDescriptionBinding
import com.awonar.app.databinding.AwonarItemInputRangeBinding
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.socialtrade.filter.adapter.holder.*

class SocialTradeFilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SocialTradeFilterItem> = mutableListOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(SocialTradeFilterDiffCallback(old, value))
                .dispatchUpdatesTo(this)
        }


    var onClick: ((String?) -> Unit)? = null
    var onChecked: ((SocialTradeFilterItem.MultiSelectorListItem) -> Unit)? = null
    var onSingleChecked: ((SocialTradeFilterItem.SingleSelectorListItem) -> Unit)? = null
    var onCustomChange: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            SocialTradeFilterType.RANGE_INPUT_TYPE -> RangeInputViewHolder(
                AwonarItemInputRangeBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false),
            )
            SocialTradeFilterType.DESCRIPTION_TYPE -> DescriptionViewHolder(
                AwonarItemDescriptionBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false),
            )
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
            SocialTradeFilterType.LIST_MULTI_SELECTOR_TYPE -> MultiSelectorViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            SocialTradeFilterType.LIST_SINGLE_SELECTOR_TYPE -> SingleSelectorViewHolder(
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
            is MultiSelectorViewHolder -> holder.bind(item as SocialTradeFilterItem.MultiSelectorListItem,
                onChecked)
            is SingleSelectorViewHolder -> holder.bind(item as SocialTradeFilterItem.SingleSelectorListItem,
                onSingleChecked)
            is DescriptionViewHolder -> holder.bind(item as SocialTradeFilterItem.DescriptionItem)
            is RangeInputViewHolder -> holder.bind(
                item as SocialTradeFilterItem.RangeInputItem,
                onCustomChange
            )
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