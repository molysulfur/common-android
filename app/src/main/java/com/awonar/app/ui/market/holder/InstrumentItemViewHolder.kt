package com.awonar.app.ui.market.holder

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.adapter.InstrumentItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class InstrumentItemViewHolder constructor(private val binding: AwonarItemInstrumentListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InstrumentItem.InstrumentListItem, viewModel: MarketViewModel?) {
        viewModel?.viewModelScope?.launch {
            viewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.findLast { quote ->
                    quote.id == item.instrument.id
                }
                binding.awonarInstrumentItemList.setChange(
                    (quote?.close ?: 0f) - (quote?.previous ?: 0f)
                )
                binding.awonarInstrumentItemList.setBid(quote?.bid ?: 0f)
                binding.awonarInstrumentItemList.setAsk(quote?.ask ?: 0f)
                val percent: Float = ConverterQuoteUtil.percentChange(
                    oldPrice = quote?.previous ?: 0f,
                    newPrice = quote?.close ?: 0f
                )
                binding.awonarInstrumentItemList.setPercentChange(percent)
            }
        }

        binding.awonarInstrumentItemList.setImage(item.instrument.logo ?: "")
        binding.awonarInstrumentItemList.setTitle(item.instrument.symbol ?: "")
        binding.awonarInstrumentItemList.setDigit(item.instrument.digit)
    }
}