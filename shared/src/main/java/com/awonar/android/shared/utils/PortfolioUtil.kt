package com.awonar.android.shared.utils

import timber.log.Timber
import kotlin.math.pow


object PortfolioUtil {

    fun pipChange(
        current: Float,
        openRate: Float,
        isBuy: Boolean,
        digit: Int
    ): Int {
        val pow = (10f.pow(digit))
        return when (isBuy) {
            true -> {
                current.minus(openRate).times(pow).toInt()
            }
            else -> {
                openRate.minus(current).times(pow).toInt()
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
}