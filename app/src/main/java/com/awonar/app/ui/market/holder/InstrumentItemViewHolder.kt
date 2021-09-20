package com.awonar.app.ui.market.holder

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class InstrumentItemViewHolder constructor(
    private val binding: AwonarItemInstrumentListBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(
        item: InstrumentItem.InstrumentListItem,
        viewModel: MarketViewModel?,
        onInstrumentClick: ((Int) -> Unit)?
    ) {
        viewModel?.viewModelScope?.launch {
            viewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.findLast { quote ->
                    quote.id == item.instrument.id
                }

                binding.awonarInstrumentItemList.setChange(
                    (quote?.close ?: 0f) - (quote?.previous ?: 0f)
                )
                // Validate Bid for anim
                quote?.bid.let {
                    if (it != item.bid) {
                        item.bid = it ?: 0f
                        binding.awonarInstrumentItemList.startAnimationBid(item.bid)
                        binding.awonarInstrumentItemList.setBid(item.bid)
                    }
                }
                // Validate Ask for anim
                quote?.ask.let {
                    if (it != item.ask) {
                        item.ask = it ?: 0f
                        binding.awonarInstrumentItemList.startAnimationAsk(item.ask)
                        binding.awonarInstrumentItemList.setAsk(item.ask)
                    }
                }

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
        binding.awonarInstrumentItemList.setOnClickListener {
            onInstrumentClick?.invoke(item.instrument.id)
        }
    }
}