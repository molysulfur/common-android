package com.awonar.app.ui.portfolio.chart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.adapter.holder.CopierPositionViewHolder
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_BUTTON
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_LIST
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_LOADING
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_SUBTITLE
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_TITLE
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartType.POSITION_CHART_VIEW
import com.awonar.app.ui.portfolio.chart.adapter.holder.*
import com.github.mikephil.charting.charts.PieChart

class PositionChartAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLists: MutableList<PositionChartItem> = mutableListOf()
        set(value) {
            val oldList = field
            field = value
            DiffUtil.calculateDiff(PortfolioChartDiffCallback(oldList, value))
                .dispatchUpdatesTo(this)
        }


    var onPieClick: ((String?) -> Unit)? = null
    var onExposure: (() -> Unit)? = null
    var onAllocate: (() -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            POSITION_CHART_LOADING -> LoadingViewHolder(
                AwonarItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            POSITION_CHART_BUTTON -> ButtonViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            POSITION_CHART_LIST -> ListItemViewHolder(
                AwonarItemMarkerListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            POSITION_CHART_VIEW -> PieChartViewHolder(
                AwonarItemPiechartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            POSITION_CHART_TITLE -> TitleViewHolder(
                AwonarItemCenterTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            POSITION_CHART_SUBTITLE -> SubTitleViewHolder(
                AwonarItemCenterSubtitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Exception("View Type is not found with $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as PositionChartItem.TitleItem)
            is SubTitleViewHolder -> holder.bind(item as PositionChartItem.SubTitleItem)
            is PieChartViewHolder -> holder.bind(item as PositionChartItem.PieChartItem, onPieClick)
            is ListItemViewHolder -> holder.bind(item as PositionChartItem.ListItem)
            is ButtonViewHolder -> holder.bind(item as PositionChartItem.ButtonItem) {
                when (it.lowercase()) {
                    "exposure" -> onExposure?.invoke()
                    else -> onAllocate?.invoke()
                }
            }
        }
    }

    override fun getItemCount(): Int = itemLists.size
    override fun getItemViewType(position: Int): Int = itemLists[position].type

    class PortfolioChartDiffCallback(
        private val oldItems: MutableList<PositionChartItem>?,
        private val newItems: MutableList<PositionChartItem>?,
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