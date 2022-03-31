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
            field = value
            notifyDataSetChanged()
        }


    var onItemSelected: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FinancialMarketType.TITLE -> TitleViewHolder(
                AwonarItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            is BarChartViewHolder -> holder.bind(item as FinancialMarketItem.BarChartItem)
            is SelectorListViewHolder -> holder.bind(item as FinancialMarketItem.ListSelectorItem,
                onItemSelected)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type


}