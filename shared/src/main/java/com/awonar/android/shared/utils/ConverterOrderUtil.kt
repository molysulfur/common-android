package com.awonar.android.shared.utils

import com.awonar.android.model.tradingdata.TradingData
import java.lang.Error

object ConverterOrderUtil {

    fun convertAmountToRate(
        amount: Float,
        conversionRate: Float,
        units: Float,
        rate: Float,
        isBuy: Boolean,
    ): Float {
        return when (isBuy) {
            true -> {
                (amount.times(conversionRate)
                    .div(units)).plus(rate)
            }
            else -> {
                -(rate.minus(amount.times(conversionRate)
                    .div(units)))
            }
        }
    }

    fun convertRateToAmount(

    ) {

    }

    fun getDefaultTakeProfit(
        amount: Float,
        defaultTakeProfitPercentage: Float,
        conversionRate: Float,
        units: Float,
        price: Float,
    ): Pair<Float, Float> {
        val percent = defaultTakeProfitPercentage.minus(0.5f).div(100)
        val amountTp = amount.times(percent)
        val rateTp = amountTp.times(conversionRate).div(units).plus(price)
        return Pair(amountTp, rateTp)
    }

    fun getDefaultStopLoss(
        amount: Float,
        defaultStopLossPercentage: Float,
        conversionRate: Float,
        units: Float,
        price: Float,
    ): Pair<Float, Float> {
        val percent = (defaultStopLossPercentage.minus(0.5f).div(100))
        val amountSL = amount.times(percent)
        val rateSL = amountSL.times(conversionRate).div(units).plus(price)
        return Pair(-amountSL, rateSL)
    }

    fun getExposure(
        leverage: Int,
        minLeverage: Int,
        amount: Float,
    ) = if (leverage < minLeverage) amount.times(leverage) else amount

    fun getMaxAmountSl(
        native: Float,
        leverage: Int,
        isBuy: Boolean,
        tradingData: TradingData?,
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

