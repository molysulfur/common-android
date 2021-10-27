package com.awonar.app.ui.portfolio.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemEmptyBinding
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.holder.CopyTradePortfolioViewHolder
import com.awonar.app.ui.portfolio.adapter.holder.EmptyViewHolder
import com.awonar.app.ui.portfolio.adapter.holder.InstrumentPortfolioViewHolder

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
            field = value
            notifyDataSetChanged()
        }

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
            else -> throw Exception("View Type is not found with $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is InstrumentPortfolioViewHolder -> holder.bind(
                item as OrderPortfolioItem.InstrumentPortfolioItem,
                columns,
                quote
            )
            is CopyTradePortfolioViewHolder -> holder.bind(
                item as OrderPortfolioItem.CopierPortfolioItem,
                columns,
                quote
            )
        }
    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun getItemCount(): Int = itemLists.size

}