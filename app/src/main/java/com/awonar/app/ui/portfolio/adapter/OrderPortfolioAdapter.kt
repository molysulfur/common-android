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
class OrderPortfolioAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pieChartType: String = "exposure"

    var itemLists: MutableList<OrderPortfolioItem> = mutableListOf(OrderPortfolioItem.EmptyItem())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var columns: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quote: Array<Quote> = emptyArray()
        set(value) {
            val oldList = field
            field = value
            OrderPortfolioDiffCallback(oldList, value)
        }

    var onClick: ((String, String) -> Unit)? = null
    var onButtonClick: ((String) -> Unit)? = null
    var onViewAllClick: (() -> Unit)? = null
    var onPieChartClick: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            OrderPortfolioType.EMPTY_PORTFOLIO -> EmptyViewHolder(
                AwonarItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.INSTRUMENT_PORTFOLIO -> InstrumentPortfolioViewHolder(
                AwonarItemInstrumentOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.ORDER_PORTFOLIO -> OrderPortfolioViewHolder(
                AwonarItemInstrumentOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.COPYTRADE_PORTFOLIO -> CopyTradePortfolioViewHolder(
                AwonarItemInstrumentOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.INSTRUMENT_POSITION_CARD -> InstrumentPositionCardViewHolder(
                AwonarItemInstrumentPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.COPY_POSITION_CARD -> CopierPositionViewHolder(
                AwonarItemCopierCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.LIST_ITEM_PORTFOLIO -> ListItemViewHolder(
                AwonarItemListPiechartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.TITLE_CENTER_PORTFOLIO -> TitleViewHolder(
                AwonarItemCenterTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.SUBTITLE_CENTER_PORTFOLIO -> SubTitleViewHolder(
                AwonarItemCenterSubtitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.BUTTON_PORTFOLIO -> PieChartTypeButtonViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.VIEWALL_BUTTON -> ViewAllViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.PIECHART_PORTFOLIO -> PieChartViewHolder(
                AwonarItemPiechartBinding.inflate(
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
            is InstrumentPortfolioViewHolder -> holder.bind(
                item as OrderPortfolioItem.InstrumentPortfolioItem,
                columns,
                quote,
                onClick
            )
            is CopyTradePortfolioViewHolder -> holder.bind(
                item as OrderPortfolioItem.CopierPortfolioItem,
                columns,
                quote,
                onClick
            )
            is InstrumentPositionCardViewHolder -> holder.bind(
                item as OrderPortfolioItem.InstrumentPositionCardItem,
                quote
            )
            is CopierPositionViewHolder -> holder.bind(
                item as OrderPortfolioItem.CopierPositionCardItem,
                quote
            )
            is ListItemViewHolder -> holder.bind(
                item as OrderPortfolioItem.ListItem
            )
            is TitleViewHolder -> holder.bind(
                item as OrderPortfolioItem.TitleItem
            )
            is SubTitleViewHolder -> holder.bind(
                item as OrderPortfolioItem.SubTitleItem
            )
            is PieChartTypeButtonViewHolder -> holder.bind(
                item as OrderPortfolioItem.ButtonItem,
                onButtonClick
            )
            is PieChartViewHolder -> holder.bind(
                item as OrderPortfolioItem.PieChartItem,
                onPieChartClick
            )
            is OrderPortfolioViewHolder -> holder.bind(
                item as OrderPortfolioItem.InstrumentOrderItem,
                columns,
                quote,
                onClick
            )
            is ViewAllViewHolder -> holder.bind(
                item as OrderPortfolioItem.ViewAllItem,
                onViewAllClick
            )
        }
    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun getItemCount(): Int = itemLists.size

    fun sortColumn(column: String?, isDesc: Boolean) {
        when (isDesc) {
            true -> itemLists.sortByDescending {
                when (it) {
                    is OrderPortfolioItem.InstrumentPortfolioItem -> sortPosition(it, column)
                    is OrderPortfolioItem.CopierPortfolioItem -> sortCopier(it, column)
                    else -> 0f
                }
            }
            else -> itemLists.sortBy {
                when (it) {
                    is OrderPortfolioItem.InstrumentPortfolioItem -> sortPosition(it, column)
                    is OrderPortfolioItem.CopierPortfolioItem -> sortCopier(it, column)
                    else -> 0f
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun sortCopier(
        item: OrderPortfolioItem.CopierPortfolioItem,
        column: String?
    ): Float = when (column) {
        "Invested" -> item.invested
        "P/L($)" -> item.profitLoss
        "P/L(%)" -> item.profitLossPercent
        "Value" -> item.value
        "Fee" -> item.fees
        "Net Invest" -> item.netInvested
        "CSL" -> item.copyStopLoss
        "CSL(%)" -> item.copyStopLossPercent
        "Avg. Open" -> item.avgOpen
        else -> 0f
    }

    private fun sortPosition(
        item: OrderPortfolioItem.InstrumentPortfolioItem,
        column: String?
    ): Float = when (column) {
        "Invested" -> item.invested
        "Units" -> item.units
        "Open" -> item.open
        "Current" -> item.current
        "P/L($)" -> item.profitLoss
        "P/L(%)" -> item.profitLossPercent
        "Pip Change" -> item.pipChange
        "Leverage" -> item.leverage.toFloat()
        "Value" -> item.value
        "Fee" -> item.fees
        "Execute at" -> item.invested
        "SL" -> item.stopLoss
        "TP" -> item.takeProfit
        "SL($)" -> item.amountStopLoss
        "TP($)" -> item.amountTakeProfit
        "SL(%)" -> item.stopLossPercent
        "TP(%)" -> item.takeProfitPercent
        "Avg. Open" -> item.open
        else -> 0f
    }

    class OrderPortfolioDiffCallback(
        private val oldItems: Array<Quote>?,
        private val newItems: Array<Quote>?
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems?.size ?: 0

        override fun getNewListSize(): Int = newItems?.size ?: 0

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) === newItems?.getOrNull(newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) == newItems?.getOrNull(newItemPosition)
        }
    }

}