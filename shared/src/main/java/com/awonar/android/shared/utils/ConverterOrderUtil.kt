package com.awonar.android.shared.utils

import com.awonar.android.model.tradingdata.TradingData
import timber.log.Timber
import java.lang.Error

object ConverterOrderUtil {

    fun getOverNightFee(
        tradingData: TradingData,
        units: Float,
        leverage: Int,
        isBuy: Boolean
    ): Pair<Float, Float> = when {
        isBuy && leverage == 1 -> Pair(
            tradingData.nonLeveragedBuyOverNightFee.times(units),
            tradingData.nonLeveragedBuyEndOfWeekFee.times(units)
        )
        isBuy && leverage > 1 -> Pair(
            tradingData.leveragedBuyOverNightFee.times(units),
            tradingData.leveragedBuyEndOfWeekFee.times(units)
        )
        !isBuy && leverage == 1 -> Pair(
            tradingData.nonLeveragedSellOverNightFee.times(units),
            tradingData.nonLeveragedSellEndOfWeekFee.times(units)
        )
        else -> Pair(
            tradingData.leveragedSellOverNightFee.times(units),
            tradingData.leveragedSellEndOfWeekFee.times(units)
        )
    }

    fun convertAmountToRate(
        amount: Float,
        conversionRate: Float,
        units: Float,
        openRate: Float,
        isBuy: Boolean,
    ): Float {
        return when (isBuy) {
            true -> {
                (amount.times(conversionRate)
                    .div(units)).plus(openRate)
            }
            else -> {
                openRate.minus(
                    amount.times(conversionRate)
                        .div(units)
                )
            }
        }
    }

    fun convertRateToAmount(
        rate: Float,
        conversionRate: Float,
        units: Float,
        openRate: Float,
        isBuy: Boolean,
    ): Float {
        return when (isBuy) {
            true -> rate.minus(openRate).times(units).div(conversionRate)
            else -> openRate.minus(rate).times(units).div(conversionRate)
        }
    }

    fun getDefaultTakeProfit(
        amount: Float,
        defaultTakeProfitPercentage: Float,
        conversionRate: Float,
        units: Float,
        price: Float,
        isBuy: Boolean
    ): Pair<Float, Float> {
        val percent = defaultTakeProfitPercentage.minus(0.5f).div(100)
        val amountTp = amount.times(percent)
        val rateTp = convertAmountToRate(
            amount = amountTp,
            conversionRate = conversionRate,
            units = units,
            openRate = price,
            isBuy = isBuy
        )
        return Pair(amountTp, rateTp)
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

