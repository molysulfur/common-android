package com.awonar.app.ui.market.holder

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentCardBinding
import com.awonar.app.ui.market.MarketViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class InstrumentCardViewHolder constructor(private val binding: AwonarItemInstrumentCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        instrument: Instrument,
        viewModel: MarketViewModel?,
        onInstrumentClick: ((Int) -> Unit)?
    ) {
        viewModel?.viewModelScope?.launch {
            viewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.findLast { quote ->
                    quote.id == instrument.id
                }
                binding.awonarInstrumentCardItem.setChange(
                    (quote?.close ?: 0f) - (quote?.previous ?: 0f)
                )
                binding.awonarInstrumentCardItem.setPrice(quote?.close ?: 0f)
                val percent: Float = ConverterQuoteUtil.percentChange(
                    oldPrice = quote?.previous ?: 0f,
                    newPrice = quote?.close ?: 0f
                )
                binding.awonarInstrumentCardItem.setPercentChange(percent)
            }
        }
        viewModel?.subscribe(instrument.id)
        binding.awonarInstrumentCardItem.setTitle(instrument.symbol ?: "")
        binding.awonarInstrumentCardItem.setDigit(instrument.digit)
        binding.awonarInstrumentCardItem.setSubTitle(instrument.name ?: "")
        binding.awonarInstrumentCardItem.setImage(instrument.logo ?: "")
        binding.awonarInstrumentCardItem.setOnClickListener {
            onInstrumentClick?.invoke(instrument.id)
        }
    }

}