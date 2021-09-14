package com.awonar.app.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemRecyclerBinding
import com.awonar.app.ui.market.holder.InstrumentWrapperViewHolder

class InstrumentHorizontalWrapperAdapter(
    private val adapter: InstrumentHorizontalAdapter
) : RecyclerView.Adapter<InstrumentWrapperViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentWrapperViewHolder =
        InstrumentWrapperViewHolder(
            AwonarItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: InstrumentWrapperViewHolder, position: Int) {
        holder.bind(adapter)
    }

    override fun getItemCount(): Int = 1
}
