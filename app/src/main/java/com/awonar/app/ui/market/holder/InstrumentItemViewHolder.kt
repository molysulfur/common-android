package com.awonar.app.ui.market.holder

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.adapter.InstrumentItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class InstrumentItemViewHolder constructor(
    private val binding: AwonarItemInstrumentListBinding,
    val viewModel: MarketViewModel?
) : RecyclerView.ViewHolder(binding.root) {
    private var item: InstrumentItem.InstrumentListItem? = null

    init {
        viewModel?.viewModelScope?.launch {
            viewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.findLast { quote ->
                    quote.id == item?.instrument?.id
                }
                quote?.run {
                    binding.awonarInstrumentItemList.setChange(
                        close - previous
                    )
                    // Validate Bid for anim
                    bid.let {
                        if (it != item?.bid) {
                            item?.bid = it
                            binding.awonarInstrumentItemList.startAnimationBid(item?.bid ?: 0f)
                        }
                        binding.awonarInstrumentItemList.setBid(item?.bid ?: 0f)
                    }
                    // Validate Ask for anim
                    ask.let {
                        if (it != item?.ask) {
                            item?.ask = it
                            binding.awonarInstrumentItemList.startAnimationAsk(item?.ask ?: 0f)
                        }
                        binding.awonarInstrumentItemList.setAsk(item?.ask ?: 0f)
                    }
                    val percent: Float = ConverterQuoteUtil.percentChange(
                        oldPrice = previous,
                        newPrice = close
                    )
                    binding.awonarInstrumentItemList.setPercentChange(percent)
                }
            }
        }
    }

    fun bind(
        item: InstrumentItem.InstrumentListItem,
        onInstrumentClick: ((Int) -> Unit)?,
        onOpenOrder: ((Instrument, String) -> Unit)?
    ) {
        this.item = item
        viewModel?.subscribe(item.instrument.id)
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