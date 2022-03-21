package com.awonar.app.ui.marketprofile.stat.financial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.marketprofile.stat.financial.holder.*
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class FinancialMarketAdapter constructor(private val fragmentActivity: FragmentActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<FinancialMarketItem>()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(FinancialDiffCallback(old, value)).dispatchUpdatesTo(this)
        }


    var onSelected: ((String?) -> Unit)? = null
    var onItemSelected: ((FinancialMarketItem.BarEntryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FinancialMarketType.TITLE -> TitleViewHolder(
                AwonarItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            FinancialMarketType.INFO_CARD -> FinancialCardViewHolder(
                AwonarItemFinancialCardBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            FinancialMarketType.TOGGLE_QUATER_TYPE -> ButtonGroupViewHolder(
                AwonarItemButtonGroupBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            FinancialMarketType.TABS -> MenuTabsViewHolder(
                AwonarItemViewpagerBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            FinancialMarketType.BARCHART -> BarChartViewHolder(
                AwonarItemMpBarchartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            FinancialMarketType.LIST_SELECTOR -> SelectorListViewHolder(
                AwonarItemSelectorListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as FinancialMarketItem.TitleMarketItem)
            is MenuTabsViewHolder -> holder.bind(item as FinancialMarketItem.TabsItem, onSelected)
            is FinancialCardViewHolder -> holder.bind(item as FinancialMarketItem.FinancialCardItem)
            is ButtonGroupViewHolder -> holder.bind(item as FinancialMarketItem.ButtonGroupItem)
            is BarChartViewHolder -> holder.bind(item as FinancialMarketItem.BarChartItem)
            is SelectorListViewHolder -> holder.bind(item as FinancialMarketItem.ListSelectorItem,
                position,
                onItemSelected)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

    class FinancialDiffCallback(
        private val oldItems: MutableList<FinancialMarketItem>?,
        private val newItems: MutableList<FinancialMarketItem>?,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems?.size ?: 0

        override fun getNewListSize(): Int = newItems?.size ?: 0

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.get(oldItemPosition) === newItems?.get(newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.get(oldItemPosition) == newItems?.get(newItemPosition)
        }
    }

}