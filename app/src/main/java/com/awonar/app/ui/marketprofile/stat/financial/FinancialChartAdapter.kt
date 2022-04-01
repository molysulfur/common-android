package com.awonar.app.ui.marketprofile.stat.financial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemMpBarchartBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.stat.financial.holder.BarChartViewHolder
import com.awonar.app.ui.marketprofile.stat.financial.holder.TitleViewHolder


class FinancialChartAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList = mutableListOf<FinancialMarketItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FinancialMarketType.TITLE -> TitleViewHolder(
                AwonarItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            FinancialMarketType.BARCHART -> BarChartViewHolder(
                AwonarItemMpBarchartBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("view type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is BarChartViewHolder -> holder.bind(item as FinancialMarketItem.BarChartItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

}