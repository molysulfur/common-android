package com.awonar.app.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.app.databinding.AwonarItemInstrumentCardBinding
import com.awonar.app.ui.market.holder.InstrumentCardViewHolder

class InstrumentHorizontalAdapter : RecyclerView.Adapter<InstrumentCardViewHolder>() {

    var itemList: List<Instrument> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentCardViewHolder =
        InstrumentCardViewHolder(
            AwonarItemInstrumentCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: InstrumentCardViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}