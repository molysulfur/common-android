package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import timber.log.Timber

class InstrumentPortfolioViewHolder constructor(
    private val binding: AwonarItemInstrumentOrderBinding,
) : RecyclerView.ViewHolder(binding.root) {


    var positionItem: OrderPortfolioItem.InstrumentPortfolioItem? = null

    fun bind(
        item: OrderPortfolioItem.InstrumentPortfolioItem,
        columns: List<String>,
        onClick: ((Int, String) -> Unit)?,
    ) {
        this.positionItem = item
        setupDataSteaming()
        with(binding.awonarInsturmentOrderItem) {
            val position = item.position
            setImage(position.instrument.logo ?: "")
            setTitle(position.instrument.symbol ?: "")
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
        binding.item = item
    }

    private fun setupDataSteaming() {
        CoroutineScope(Dispatchers.Default).launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                positionItem?.let { positionItem ->
                    val quote = quotes[positionItem.position.instrument.id]
                    quote?.let {
                        positionItem.current = if (positionItem.position.isBuy) it.bid else it.ask
                        val pl = PortfolioUtil.getProfitOrLoss(
                            positionItem.current,
                            positionItem.open,
                            positionItem.units,
                            positionItem.conversionRate,
                            positionItem.position.isBuy
                        )
                        val pipChange = PortfolioUtil.pipChange(
                            positionItem.current,
                            positionItem.open,
                            positionItem.position.isBuy,
                            positionItem.position.instrument.digit
                        )
                        val value = PortfolioUtil.getValue(pl, positionItem.invested)
                        val plPercent = PortfolioUtil.profitLossPercent(pl, positionItem.invested)
                        positionItem.profitLoss = pl
                        positionItem.pipChange = pipChange
                        positionItem.value = value
                        positionItem.profitLossPercent = plPercent
                    }
                    binding.item = positionItem
                }
            }
        }
    }

}