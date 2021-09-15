package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.app.databinding.AwonarItemInstrumentCardBinding
import timber.log.Timber

class InstrumentCardViewHolder constructor(private val binding: AwonarItemInstrumentCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(instrument: Instrument) {
        Timber.e("$instrument")
        binding.awonarInstrumentCardItem.setTitle(instrument.symbol ?: "")
        binding.awonarInstrumentCardItem.setSubTitle(instrument.name ?: "")
        binding.awonarInstrumentCardItem.setImage(instrument.logo ?: "")
    }
}