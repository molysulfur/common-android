package com.awonar.app.ui.portfolio.chart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterTitleBinding
import com.awonar.app.databinding.AwonarItemCopierCardBinding
import com.awonar.app.ui.portfolio.adapter.holder.CopierPositionViewHolder
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_TITLE
import com.awonar.app.ui.portfolio.chart.adapter.holder.TitleViewHolder

class PositionChartAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLists: MutableList<PositionChartItem> = mutableListOf()
        set(value) {
            val oldList = field
            field = value
//            DiffUtil.calculateDiff(PortfolioAdapter.PortfolioDiffCallback(oldList, value))
//                .dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            POSITION_CHART_TITLE -> TitleViewHolder(
                AwonarItemCenterTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
//            POSITION_CHART_SUBTITLE -> TitleViewHolder(
//                AwonarItemCenterTitleBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
            else -> throw Exception("View Type is not found with $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = itemLists.size
    override fun getItemViewType(position: Int): Int = itemLists[position].type
}