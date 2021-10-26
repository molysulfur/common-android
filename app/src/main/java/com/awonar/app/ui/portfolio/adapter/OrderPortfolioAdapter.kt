package com.awonar.app.ui.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.holder.InstrumentPortfolioViewHolder

class OrderPortfolioAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLists: MutableList<OrderPortfolioItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quote: Array<Quote> = emptyArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    fun shownMenu(position: Int) {
        val menu = itemLists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            OrderPortfolioType.INSTRUMENT_PORTFOLIO -> InstrumentPortfolioViewHolder(
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
            is InstrumentPortfolioViewHolder -> holder.bind(item as OrderPortfolioItem.InstrumentPortfolioItem,quote)
        }
    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun getItemCount(): Int = itemLists.size

}