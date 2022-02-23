package com.awonar.app.ui.portfolio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.*
import com.awonar.app.ui.portfolio.adapter.holder.*

@SuppressLint("NotifyDataSetChanged")
class PortfolioAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pieChartType: String = "exposure"

    var itemLists: MutableList<PortfolioItem> = mutableListOf(PortfolioItem.EmptyItem())
        set(value) {
            val oldList = field
            field = value
            DiffUtil.calculateDiff(PortfolioDiffCallback(oldList, value)).dispatchUpdatesTo(this)
        }

    var columns: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quote: MutableMap<Int, Quote> = mutableMapOf()
        set(value) {
            field = value
        }

    var onClick: ((Int, String) -> Unit)? = null
    var onButtonClick: ((String) -> Unit)? = null
    var onViewAllClick: (() -> Unit)? = null
    var onPieChartClick: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PortfolioType.SECTION_PORTFOLIO -> SectionViewHolder(
                AwonarItemSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.EMPTY_PORTFOLIO -> EmptyViewHolder(
                AwonarItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.INSTRUMENT_PORTFOLIO -> InstrumentPortfolioViewHolder(
                AwonarItemPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.ORDER_PORTFOLIO -> OrderPortfolioViewHolder(
                AwonarItemPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.COPYTRADE_PORTFOLIO -> CopyTradePortfolioViewHolder(
                AwonarItemPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.INSTRUMENT_POSITION_CARD -> InstrumentPositionCardViewHolder(
                AwonarItemInstrumentPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.COPY_POSITION_CARD -> CopierPositionViewHolder(
                AwonarItemCopierCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.LIST_ITEM_PORTFOLIO -> ListItemViewHolder(
                AwonarItemListPiechartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.TITLE_CENTER_PORTFOLIO -> TitleViewHolder(
                AwonarItemCenterTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.SUBTITLE_CENTER_PORTFOLIO -> SubTitleViewHolder(
                AwonarItemCenterSubtitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.BUTTON_PORTFOLIO -> PieChartTypeButtonViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.VIEWALL_BUTTON -> ViewAllViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PortfolioType.PIECHART_PORTFOLIO -> PieChartViewHolder(
                AwonarItemPiechartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw Exception("View Type is not found with $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is InstrumentPortfolioViewHolder -> holder.bind(
                item as PortfolioItem.InstrumentPortfolioItem,
                columns,
                onClick
            )
            is CopyTradePortfolioViewHolder -> holder.bind(
                item as PortfolioItem.CopierPortfolioItem,
                columns,
                onClick
            )
            is InstrumentPositionCardViewHolder -> holder.bind(
                item as PortfolioItem.InstrumentPositionCardItem,
                quote[item.position.instrument?.id],
            )
            is CopierPositionViewHolder -> holder.bind(
                item as PortfolioItem.CopierPositionCardItem,
            )
            is ListItemViewHolder -> holder.bind(
                item as PortfolioItem.ListItem
            )
            is TitleViewHolder -> holder.bind(
                item as PortfolioItem.TitleItem
            )
            is SubTitleViewHolder -> holder.bind(
                item as PortfolioItem.SubTitleItem
            )
            is PieChartTypeButtonViewHolder -> holder.bind(
                item as PortfolioItem.ButtonItem,
                onButtonClick
            )
            is PieChartViewHolder -> holder.bind(
                item as PortfolioItem.PieChartItem,
                onPieChartClick
            )
            is OrderPortfolioViewHolder -> holder.bind(
                item as PortfolioItem.InstrumentItem,
                columns,
            )
            is ViewAllViewHolder -> holder.bind(
                item as PortfolioItem.ViewAllItem,
                onViewAllClick
            )
            is SectionViewHolder -> holder.bind(
                item as PortfolioItem.SectionItem
            )
        }
    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun getItemCount(): Int = itemLists.size

    fun sortColumn(column: String?, isDesc: Boolean) {
        when (isDesc) {
            true -> itemLists.sortByDescending {
                when (it) {
                    is PortfolioItem.InstrumentPortfolioItem -> sortPosition(it, column)
                    is PortfolioItem.CopierPortfolioItem -> sortCopier(it, column)
                    else -> 0f
                }
            }
            else -> itemLists.sortBy {
                when (it) {
                    is PortfolioItem.InstrumentPortfolioItem -> sortPosition(it, column)
                    is PortfolioItem.CopierPortfolioItem -> sortCopier(it, column)
                    else -> 0f
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun sortCopier(
        item: PortfolioItem.CopierPortfolioItem,
        column: String?,
    ): Float = when (column) {
        "Invested" -> item.copier.invested
        "P/L($)" -> item.copier.profitLoss
        "P/L(%)" -> item.copier.profitLossPercent
        "Value" -> item.copier.value
        "Fee" -> item.copier.fees
        "Net Invest" -> item.copier.netInvested
        "CSL" -> item.copier.copyStopLoss
        "CSL(%)" -> item.copier.copyStopLossPercent
        "Avg. Open" -> item.copier.avgOpen
        else -> 0f
    }

    private fun sortPosition(
        item: PortfolioItem.InstrumentPortfolioItem,
        column: String?,
    ): Float = when (column) {
        "Invested" -> item.position.invested
        "Units" -> item.position.units
        "Open" -> item.position.open
        "Current" -> item.position.current
        "P/L($)" -> item.position.profitLoss
        "P/L(%)" -> item.position.profitLossPercent
        "Pip Change" -> item.position.pipChange
        "Leverage" -> item.position.leverage.toFloat()
        "Value" -> item.position.value
        "Fee" -> item.position.fees
        "Execute at" -> item.position.invested
        "SL" -> item.position.stopLoss
        "TP" -> item.position.takeProfit
        "SL($)" -> item.position.amountStopLoss
        "TP($)" -> item.position.amountTakeProfit
        "SL(%)" -> item.position.stopLossPercent
        "TP(%)" -> item.position.takeProfitPercent
        "Avg. Open" -> item.position.open
        else -> 0f
    }

    class PortfolioDiffCallback(
        private val oldItems: MutableList<PortfolioItem>?,
        private val newItems: MutableList<PortfolioItem>?,
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