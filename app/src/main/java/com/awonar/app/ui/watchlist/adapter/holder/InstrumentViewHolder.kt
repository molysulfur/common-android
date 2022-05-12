package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import timber.log.Timber

class InstrumentViewHolder constructor(private val binding: AwonarItemInstrumentListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var instrumentId: Int = 0

    fun bind(
        item: WatchlistItem.InstrumentItem,
        onItemClick: ((Int) -> Unit)?,
        onTradeClick: ((Int, Boolean) -> Unit)?
    ) {
        instrumentId = item.instrumentId
        CoroutineScope(Dispatchers.Main).launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val quote = quotes[instrumentId]
                quote?.let {
                    binding.awonarInstrumentItemList.setAsk(quote.ask)
                    binding.awonarInstrumentItemList.setBid(quote.bid)
                    binding.awonarInstrumentItemList.setChange(
                        ConverterQuoteUtil.change(
                            quote.close,
                            quote.previous
                        )
                    )
                    binding.awonarInstrumentItemList.setPercentChange(
                        ConverterQuoteUtil.percentChange(
                            quote.previous,
                            quote.close
                        )
                    )
                    binding.awonarInstrumentItemList.setPercentChange(quote.close)
                }
            }
        }
        with(binding.awonarInstrumentItemList) {
            setImage(item.image ?: "")
            setTitle(item.title ?: "")
            setOnClickListener {
                onItemClick?.invoke(instrumentId)
            }
            onOpenOrder = {
                onTradeClick?.invoke(item.instrumentId, it)
            }
        }
    }

}