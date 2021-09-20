package com.awonar.app.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.holder.*

class InstrumentListAdapter constructor(private val viewModel: MarketViewModel?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: List<InstrumentItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var onInstrumentClick: ((Int) -> Unit)? = null
    var onViewMoreClick: ((String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            InstrumentType.INSTRUMENT_DIVIDER_TYPE -> InstrumentDividerViewHolder(
                AwonarItemDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            InstrumentType.INSTRUMENT_BLANK_TYPE -> InstrumentBlankViewHolder(
                AwonarItemDividerBlankBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            InstrumentType.INSTRUMENT_TITLE_TYPE -> InstrumentTitleViewHolder(
                AwonarItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            InstrumentType.INSTRUMENT_VIEW_MORE_TYPE -> InstrumentViewMoreViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            InstrumentType.INSTRUMENT_LIST_ITEM_TYPE -> InstrumentItemViewHolder(
                AwonarItemInstrumentListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
            )
            InstrumentType.INSTRUMENT_LOADING_TYPE -> InstrumentLoadingViewHolder(
                AwonarItemListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("viewType is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is InstrumentTitleViewHolder -> holder.bind(item as InstrumentItem.TitleItem)
            is InstrumentViewMoreViewHolder -> holder.bind(item as InstrumentItem.InstrumentViewMoreItem,onViewMoreClick)
            is InstrumentItemViewHolder -> holder.bind(
                item as InstrumentItem.InstrumentListItem,
                viewModel,
                onInstrumentClick
            )
        }
    }

    override fun getItemCount(): Int = itemList.size
}