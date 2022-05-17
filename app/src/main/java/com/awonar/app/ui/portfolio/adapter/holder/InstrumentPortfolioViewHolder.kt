package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class InstrumentPortfolioViewHolder constructor(
    private val binding: AwonarItemPositionBinding,
) : RecyclerView.ViewHolder(binding.root) {

    var job: CoroutineScope? = null
    var positionItem: PortfolioItem.InstrumentPortfolioItem? = null

    fun bind(
        item: PortfolioItem.InstrumentPortfolioItem,
        columns: List<String>,
        onClick: ((Int, String) -> Unit)?,
    ) {
        if (item.position.amount > 0) {
            item.position.invested = item.position.amount
        }
        this.positionItem = item

        with(binding.awonarInsturmentOrderItem) {
            val position = item.position
            setImage(position.instrument?.logo ?: "")
            setTitle(position.instrument?.symbol ?: "")
            setDescription(item.meta ?: "")
            setOnClickListener {
                onClick?.invoke(item.index, "instrument")
            }
        }
        if (columns.isNotEmpty()) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
        if (item.isRealTime) {
            setupJob()
        } else {
            job?.cancel()
        }
        binding.item = item.position
    }

    private fun setupJob() {
        job = CoroutineScope(Dispatchers.IO)
        job?.launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                positionItem?.let { positionItem ->
                    val quote = quotes[positionItem.position.instrument?.id]
                    quote?.let {
                        positionItem.position.current =
                            if (positionItem.position.isBuy) it.bid else it.ask
                        val pl = PortfolioUtil.getProfitOrLoss(
                            positionItem.position.current,
                            positionItem.position.openRate,
                            positionItem.position.units,
                            positionItem.conversionRate,
                            positionItem.position.isBuy
                        )
                        val pipChange = PortfolioUtil.pipChange(
                            positionItem.position.current,
                            positionItem.position.openRate,
                            positionItem.position.isBuy,
                            positionItem.position.instrument?.digit ?: 2
                        )
                        Timber.e(
                            """
                             symbol:  ${positionItem.position.instrument?.symbol}
                             current: ${positionItem.position.current}   
                             openRate: ${positionItem.position.openRate}   
                             units: ${positionItem.position.units}   
                             conversionRate: ${positionItem.conversionRate}  
                             isBuy: ${positionItem.position.isBuy}   
                             pl: $pl
                             pip: $pipChange
                            """
                        )
                        val value = PortfolioUtil.getValue(pl, positionItem.position.invested)
                        val plPercent =
                            PortfolioUtil.profitLossPercent(pl, positionItem.position.invested)
                        positionItem.position.profitLoss = pl
                        positionItem.position.pipChange = pipChange
                        positionItem.position.value = value
                        positionItem.position.profitLossPercent = plPercent
                    }
                    binding.item = positionItem.position
                }
            }
        }
    }

}