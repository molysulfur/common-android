package com.awonar.app.ui.marketprofile.stat.financial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.marketprofile.stat.financial.holder.*

class FinancialHeaderAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<FinancialMarketItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onToggleButton: ((String?) -> Unit)? = null
    var onSelected: ((String?) -> Unit)? = null

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
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as FinancialMarketItem.TitleMarketItem)
            is ButtonGroupViewHolder -> holder.bind(item as FinancialMarketItem.ButtonGroupItem,
                onToggleButton)
            is MenuTabsViewHolder -> holder.bind(item as FinancialMarketItem.TabsItem, onSelected)
        }
        class FinancialHeaderAdapter :
            RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            var itemList = mutableListOf<FinancialMarketItem>()
                set(value) {
                    field = value
                    notifyDataSetChanged()
                }

            var onToggleButton: ((String?) -> Unit)? = null
            var onSelected: ((String?) -> Unit)? = null

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
                    else -> throw Error("view type is not found!")
                }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = itemList[position]
                when (holder) {
                    is TitleViewHolder -> holder.bind(item as FinancialMarketItem.TitleMarketItem)
                    is ButtonGroupViewHolder -> holder.bind(item as FinancialMarketItem.ButtonGroupItem,
                        onToggleButton)
                    is MenuTabsViewHolder -> holder.bind(item as FinancialMarketItem.TabsItem, onSelected)
                }
            }

            override fun getItemCount(): Int = itemList.size

            override fun getItemViewType(position: Int): Int = itemList[position].type

        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

}