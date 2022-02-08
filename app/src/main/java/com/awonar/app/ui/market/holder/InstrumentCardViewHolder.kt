package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InstrumentCardViewHolder constructor(private val binding: AwonarItemInstrumentCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var instrumentId: Int = 0

    fun bind(
        instrument: Instrument,
        onInstrumentClick: ((Int) -> Unit)?,
    ) {
        this.instrumentId = instrument.id
        CoroutineScope(Dispatchers.Main).launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val quote = quotes[instrumentId]
                quote?.let {
                    binding.awonarInstrumentCardItem.setPrice(quote.close)
                    binding.awonarInstrumentCardItem.setChange(ConverterQuoteUtil.change(
                        quote.close,
                        quote.previous
                    ))
                    binding.awonarInstrumentCardItem.setPercentChange(ConverterQuoteUtil.percentChange(
                        quote.previous,
                        quote.close
                    ))
                    binding.awonarInstrumentCardItem.setPercentChange(quote.close)
                }
            }
        }
        binding.awonarInstrumentCardItem.setTitle(instrument.symbol ?: "")
        binding.awonarInstrumentCardItem.setDigit(instrument.digit)
        binding.awonarInstrumentCardItem.setSubTitle(instrument.name ?: "")
        binding.awonarInstrumentCardItem.setImage(instrument.logo ?: "")
        binding.awonarInstrumentCardItem.setOnClickListener {
            onInstrumentClick?.invoke(instrument.id)
        }
    }

}