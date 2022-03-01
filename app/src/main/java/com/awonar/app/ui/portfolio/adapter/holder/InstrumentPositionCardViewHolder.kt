package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemInstrumentPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InstrumentPositionCardViewHolder constructor(private val binding: AwonarItemInstrumentPositionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var job: CoroutineScope? = null
    private var item: PortfolioItem.InstrumentPositionCardItem? = null

    fun bind(item: PortfolioItem.InstrumentPositionCardItem) {
        this.item = item
        setupJob()
        binding.item = item
    }

    private fun setupJob() {
        job = CoroutineScope(Dispatchers.IO)
        job?.launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                item?.let { positionItem ->
                    val quote = quotes[positionItem.position.instrument?.id]
                    quote?.let {
                        positionItem.position.current =
                            if (positionItem.position.isBuy) it.bid else it.ask
                        val pl = PortfolioUtil.getProfitOrLoss(
                            positionItem.position.current,
                            positionItem.position.open,
                            positionItem.position.units,
                            1f,
                            positionItem.position.isBuy
                        )
                        val pipChange = PortfolioUtil.pipChange(
                            positionItem.position.current,
                            positionItem.position.open,
                            positionItem.position.isBuy,
                            positionItem.position.instrument?.digit ?: 2
                        )
                        val value = PortfolioUtil.getValue(pl, positionItem.position.invested)
                        val plPercent =
                            PortfolioUtil.profitLossPercent(pl, positionItem.position.invested)
                        positionItem.position.profitLoss = pl
                        positionItem.position.pipChange = pipChange
                        positionItem.position.value = value
                        positionItem.position.profitLossPercent = plPercent
                        positionItem.position.change =
                            ConverterQuoteUtil.change(quote.close, quote.previous)
                        positionItem.position.changePercent =
                            ConverterQuoteUtil.percentChange(quote.previous, quote.close)
                        positionItem.position.status = quote.status
                    }
                    binding.item = item
                }
            }
        }
    }
}