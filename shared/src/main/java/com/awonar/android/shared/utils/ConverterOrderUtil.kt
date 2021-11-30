package com.awonar.android.shared.utils

import com.awonar.android.model.tradingdata.TradingData
import java.lang.Error
import kotlin.math.min

object ConverterOrderUtil {

    fun getExposure(
        leverage: Int,
        minLeverage: Int,
        amount: Float
    ) = if (leverage < minLeverage) amount.times(leverage) else amount

    fun getMaxAmountSl(
        native: Float,
        leverage: Int,
        isBuy: Boolean,
        tradingData: TradingData?
    ): Float {
        if (tradingData != null) {
            return when {
                leverage == 1 && isBuy -> -(native.times(tradingData.maxStopLossPercentageNonLeveragedBuy)
                    .div(100))
                leverage == 1 && !isBuy -> -(native.times(tradingData.maxStopLossPercentageNonLeveragedSell)
                    .div(100))
                leverage > 1 && isBuy -> -(native.times(tradingData.maxStopLossPercentageLeveragedBuy)
                    .div(100))
                leverage > 1 && !isBuy -> -(native.times(tradingData.maxStopLossPercentageLeveragedSell)
                    .div(100))
                else -> throw Error("calculate amount sl wrong case!")
            }
        } else {
            throw Error("trading data is null")
        }
    }
}

