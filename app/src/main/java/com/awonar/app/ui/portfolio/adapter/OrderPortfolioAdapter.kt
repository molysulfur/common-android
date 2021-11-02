package com.awonar.app.ui.portfolio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemEmptyBinding
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.databinding.AwonarItemInstrumentPositionBinding
import com.awonar.app.ui.portfolio.adapter.holder.CopyTradePortfolioViewHolder
import com.awonar.app.ui.portfolio.adapter.holder.EmptyViewHolder
import com.awonar.app.ui.portfolio.adapter.holder.InstrumentPortfolioViewHolder
import com.awonar.app.ui.portfolio.adapter.holder.InstrumentPositionViewHolder

@SuppressLint("NotifyDataSetChanged")
class OrderPortfolioAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            OrderPortfolioDiffCallback(field, value)
            field = value
        }


    var onClick: ((String, String) -> Unit)? = null

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
            OrderPortfolioType.COPYTRADE_PORTFOLIO -> CopyTradePortfolioViewHolder(
                AwonarItemInstrumentOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrderPortfolioType.INSTRUMENT_POSITION_CARD -> InstrumentPositionViewHolder(
                AwonarItemInstrumentPositionBinding.inflate(
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
            is InstrumentPositionViewHolder -> holder.bind(
                item as OrderPortfolioItem.InstrumentPositionCardItem,
                quote
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