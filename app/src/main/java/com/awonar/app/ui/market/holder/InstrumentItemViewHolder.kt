package com.awonar.app.ui.market.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.market.adapter.InstrumentItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InstrumentItemViewHolder constructor(
    private val binding: AwonarItemInstrumentListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var instrumentId: Int = 0

    fun bind(
        item: InstrumentItem.InstrumentListItem,
        onInstrumentClick: ((Int) -> Unit)?,
        onOpenOrder: ((Instrument, Boolean) -> Unit)?,
    ) {
        this.instrumentId = item.instrument.id
        CoroutineScope(Dispatchers.Main).launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val quote = quotes[instrumentId]
                quote?.let {
                    binding.awonarInstrumentItemList.setAsk(quote.ask)
                    binding.awonarInstrumentItemList.setBid(quote.bid)
                    binding.awonarInstrumentItemList.setChange(ConverterQuoteUtil.change(
                        quote.close,
                        quote.previous
                    ))
                    binding.awonarInstrumentItemList.setPercentChange(ConverterQuoteUtil.percentChange(
                        quote.previous,
                        quote.close
                    ))
                    binding.awonarInstrumentItemList.setPercentChange(quote.close)
                }
            }
        }
        binding.awonarInstrumentItemList.onOpenOrder = {
            onOpenOrder?.invoke(item.instrument, it)
        }
        binding.awonarInstrumentItemList.setImage(item.instrument.logo ?: "")
        binding.awonarInstrumentItemList.setTitle(item.instrument.symbol ?: "")
        binding.awonarInstrumentItemList.setDigit(item.instrument.digit)
        binding.awonarInstrumentItemList.setOnClickListener {
            onInstrumentClick?.invoke(item.instrument.id)
        }
    }
}