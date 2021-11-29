package com.awonar.android.shared.domain.order

import com.awonar.android.exception.AddAmountException
import com.awonar.android.exception.AvailableNotEnoughException
import com.awonar.android.exception.RefundException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.ValidateRateStopLossRequest
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import java.lang.Error
import java.lang.Math.abs
import javax.inject.Inject
import kotlin.math.pow

class ValidateRateStopLossUseCase @Inject constructor(
    private val repository: MarketRepository,
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRateStopLossRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateRateStopLossRequest) {
        val tradingData = repository.getTradingDataById(parameters.instrument.id)
        val conversion =
            currenciesRepository.getConversionByInstrumentId(parameters.instrument.id).rateBid
        val exposure = parameters.exposure
        val nativeAmount = exposure.div(parameters.leverage)
        val minRateSl = parameters.currentPrice
        val maxRateSL = 10f.pow(-parameters.instrument.digit)
        val maxAmountSl =
            getMaxAmountSl(nativeAmount, parameters.leverage, parameters.isBuy, tradingData)
        val slRate = parameters.rateSl
        val amountSl = slRate.minus(parameters.openPrice).times(parameters.units).div(conversion)
        if (parameters.isBuy) {
            if ((slRate > minRateSl) or (slRate < maxRateSL)) {
                throw ValidationException("Stop Loss cannot less than $minRateSl", minRateSl)
            } else {
                val diff = amountSl.minus(maxAmountSl)
                val oldAmount =
                    parameters.openPrice.minus(slRate).times(parameters.units).div(conversion)
                if (diff < 0) {
                    if (abs(-diff.minus(parameters.amountSl.minus(nativeAmount))) < parameters.available) {
                        val addAmount = nativeAmount.minus(parameters.amount).minus(diff)
                        if (addAmount >= 0) {
                            throw AddAmountException(
                                "Amount should be add \$%.2f".format(abs(addAmount)),
                                addAmount
                            )
                        }
                        throw RefundException(
                            "Order should be refund \$%.2f".format(abs(addAmount)),
                            addAmount
                        )
                    } else {
                        throw AvailableNotEnoughException("Available not enough.")
                    }
                } else {
                    val refund = oldAmount.minus(parameters.amountSl)
                    throw RefundException(
                        "Order should be refund \$%.2f".format(abs(refund)),
                        refund
                    )
                }
            }
        }
    }

    private fun getMaxAmountSl(
        native: Float,
        leverage: Int,
        isBuy: Boolean,
        tradingData: TradingData
    ): Float = when {
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
}