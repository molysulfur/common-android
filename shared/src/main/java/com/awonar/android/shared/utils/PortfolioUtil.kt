package com.awonar.android.shared.utils

import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Position
import kotlin.math.pow

object PortfolioUtil {

    fun profitLossPercent(pl: Float, invested: Float) =
        pl.div(invested).times(100)

    fun pipChange(
        current: Float,
        openRate: Float,
        isBuy: Boolean,
        digit: Int
    ): Float {
        val pow = (10f.pow(digit))
        return when (isBuy) {
            true -> {
                current.minus(openRate).times(pow)
            }
            else -> {
                openRate.minus(current).times(pow)
            }
        }
    }

    fun getProfitOrLoss(
        current: Float,
        openRate: Float,
        unit: Float,
        rate: Float,
        isBuy: Boolean
    ): Float =
        when (isBuy) {
            true -> current.minus(openRate).times(unit).div(rate)
            false -> openRate.minus(current).times(unit).div(rate)
        }

    fun getValue(profitLoss: Float, amount: Float): Float = profitLoss.plus(amount)

    fun getCurrent(isBuy: Boolean, quote: Quote): Float = if (isBuy) quote.bid else quote.ask

}