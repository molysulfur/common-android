package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarItemCopierPositionBinding
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CopyTradePortfolioViewHolder constructor(
    private val binding: AwonarItemCopierPositionBinding,
) : RecyclerView.ViewHolder(binding.root) {

    var copierItem: PortfolioItem.CopierPortfolioItem? = null

    fun bind(
        item: PortfolioItem.CopierPortfolioItem,
        columns: List<String>,
        onClick: (() -> Unit)?,
    ) {
        copierItem = item
        setupDataSteaming()
        if (columns.isNotEmpty()) {
            binding.column1 = columns[0]
            binding.column2 = columns[1]
            binding.column3 = columns[2]
            binding.column4 = columns[3]
        }
        with(binding.awonarInsturmentOrderItem) {
            val copy = item.copier
            setImage(copy.user.picture ?: "")
            setTitle(copy.user.username ?: "")
            setOnClickListener {
                onClick?.invoke()
            }
        }
        binding.item = item.copier
    }

    private fun setupDataSteaming() {
        CoroutineScope(Dispatchers.IO).launch {
            QuoteSteamingManager.quotesState.collect { quotes ->
                var sumFloatingPL = 0f
                val copier = copierItem?.copier
                copier?.positions?.forEach { position ->
                    val quote = quotes[position.instrumentId]
                    quote?.let {
                        val current = if (position.isBuy) it.bid else it.ask
                        val pl = PortfolioUtil.getProfitOrLoss(
                            current,
                            position.openRate,
                            position.units,
                            copierItem?.conversions?.get(position.instrumentId) ?: 1f,
                            position.isBuy
                        )
                        sumFloatingPL += sumFloatingPL.plus(pl)
                    }
                }
                val pl = sumFloatingPL.plus(copier?.closedPositionsNetProfit ?: 0f)
                val plPercent = pl.div(copier?.invested ?: 0f)
                val moneyInOut = copier?.depositSummary?.minus(copier.withdrawalSummary) ?: 0f
                val value = copier?.initialInvestment?.plus(moneyInOut)?.plus(pl) ?: 0f
                copier?.profitLoss = pl
                copier?.profitLossPercent = plPercent
                copier?.value = value
                binding.item = copier
            }
        }
    }

}