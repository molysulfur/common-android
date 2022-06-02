package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InstrumentPortfolioViewHolder constructor(
    private val binding: AwonarItemPositionBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val job = CoroutineScope(Dispatchers.IO)
    var positionItem: PortfolioItem.PositionItem? = null

    fun bind(
        item: PortfolioItem.PositionItem,
        columns: List<String>,
        onClick: (() -> Unit)?,
    ) {
        if (columns.isNotEmpty()) {
            positionItem = item
            positionItem?.apply {
                val positions = instrumentGroup
                invested = positions?.sumOf { it.amount.toDouble() }?.toFloat() ?: 0f
                units = positions?.sumOf { it.units.toDouble() }?.toFloat() ?: 0f
            }

            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
            setupJob()
            with(binding.awonarInsturmentOrderItem) {
                item.instrumentGroup?.let {
                    setImage(it[0].instrument?.logo ?: "")
                    setTitle(it[0].instrument?.symbol ?: "")
                    setDescription("")
                }
                setOnClickListener {
                    onClick?.invoke()
                }
            }
            positionItem?.let {
                binding.item = it
            }
        }
    }

    private fun setupJob() {
        job.launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val positions = positionItem?.instrumentGroup
                positionItem?.let { positionItem ->
                    val quote = quotes[positions?.get(0)?.instrument?.id]
                    val leverage = (positions?.sumOf {
                        val rate = positionItem.conversionRate
                        it.openRate.times(it.units).times(rate).toDouble()
                    })?.div(positionItem.invested) ?: 0.0
                    quote?.let {
                        var pl = 0f
                        positionItem.instrumentGroup?.forEach { position ->
                            val current = ConverterQuoteUtil.getCurrentPrice(
                                quote = it,
                                leverage = leverage.toInt(),
                                isBuy = position.isBuy
                            )
                            val sumOpenRate: Double = positionItem.instrumentGroup.sumOf {
                                it.openRate.toDouble().div(positionItem.instrumentGroup.size)
                            }
                            positionItem.current = it.close
                            positionItem.openRate = sumOpenRate.toFloat()
                            positionItem.leverage = leverage.toFloat()
                            pl += PortfolioUtil.getProfitOrLoss(
                                current,
                                position.openRate,
                                position.units,
                                positionItem.conversionRate,
                                position.isBuy
                            )
                        }
                        positionItem.pl = pl
                        positionItem.plPercent =
                            PortfolioUtil.profitLossPercent(
                                pl = pl,
                                invested = positionItem.invested
                            )
                    }
                    binding.item = positionItem
                }
            }
        }
    }

}