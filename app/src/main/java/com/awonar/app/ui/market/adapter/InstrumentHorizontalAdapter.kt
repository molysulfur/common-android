package com.awonar.app.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.app.databinding.AwonarItemInstrumentCardBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.holder.InstrumentCardViewHolder

class InstrumentHorizontalAdapter constructor(private val viewModel: MarketViewModel?) :
    RecyclerView.Adapter<InstrumentCardViewHolder>() {

    var itemList: List<Instrument> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onInstrumentClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentCardViewHolder =
        InstrumentCardViewHolder(
            AwonarItemInstrumentCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: InstrumentCardViewHolder, position: Int) {
        holder.bind(itemList[position],viewModel,onInstrumentClick)
    }

    override fun getItemCount(): Int = itemList.size
}