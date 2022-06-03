package com.awonar.app.ui.portfolio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.portfolio.adapter.holder.*
import com.awonar.app.ui.portfolio.chart.adapter.holder.*
import timber.log.Timber

@SuppressLint("NotifyDataSetChanged")
class PortfolioAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    var onIntrumentClick: ((Int) -> Unit)? = null
    var onCopierClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PortfolioType.BALANCE_PORTFOLIO -> BalanceViewHolder(
                AwonarItemListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
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
                AwonarItemCopierPositionBinding.inflate(
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
            else -> throw Exception("View Type is not found with $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is BalanceViewHolder -> holder.bind(item as PortfolioItem.BalanceItem)
            is InstrumentPortfolioViewHolder -> holder.bind(
                item as PortfolioItem.PositionItem,
                columns
            ) {
                onIntrumentClick?.invoke(position)
            }
            is CopyTradePortfolioViewHolder -> holder.bind(
                item as PortfolioItem.CopierPortfolioItem,
                columns
            ) {
                onCopierClick?.invoke(position)
            }
            is InstrumentPositionCardViewHolder -> holder.bind(
                item as PortfolioItem.InstrumentPositionCardItem
            )
            is CopierPositionViewHolder -> holder.bind(
                item as PortfolioItem.CopierPositionCardItem,
            )
            is OrderPortfolioViewHolder -> holder.bind(
                item as PortfolioItem.InstrumentItem,
                columns,
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
                    is PortfolioItem.PositionItem -> sortPosition(it, column)
                    is PortfolioItem.CopierPortfolioItem -> sortCopier(it, column)
                    else -> 0f
                }
            }
            else -> itemLists.sortBy {
                when (it) {
                    is PortfolioItem.PositionItem -> sortPosition(it, column)
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
        item: PortfolioItem.PositionItem,
        column: String?,
    ): Float = when (column) {
        "Invested" -> item.invested
        "Units" -> item.units
        "Open" -> item.openRate
        "Current" -> item.current
        "P/L($)" -> item.pl
        "P/L(%)" -> item.plPercent
        "Pip Change" -> item.pipChange.toFloat()
        "Leverage" -> item.leverage
        "Value" -> item.value
        "Fee" -> item.fees
        "SL" -> item.stopLoss
        "TP" -> item.takeProfit
        "SL($)" -> item.amountStopLoss
        "TP($)" -> item.amountTakeProfit
        "SL(%)" -> item.stopLossPercent
        "TP(%)" -> item.takeProfitPercent
        "Avg. Open" -> item.openRate
        else -> 0f
    }

    class PortfolioDiffCallback(
        private val oldItems: MutableList<PortfolioItem>,
        private val newItems: MutableList<PortfolioItem>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }
    }

}