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
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRateStopLossRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateRateStopLossRequest) {
        val conversion = parameters.conversionRate
        val exposure = parameters.exposure
        val nativeAmount = exposure.div(parameters.leverage)
        val minRateSl = parameters.currentPrice
        val maxRateSL = 10f.pow(-parameters.digit)
        val maxAmountSl = parameters.maxStopLoss
        val slRate = parameters.rateSl
        val amountSl = slRate.minus(parameters.openPrice).times(parameters.units).div(conversion)
        if (parameters.isBuy) {
            if ((slRate > minRateSl) or (slRate < maxRateSL)) {
                throw ValidationException("Stop Loss cannot less than $minRateSl", minRateSl)
            } else {
                val diff = amountSl.minus(maxAmountSl)
                if (diff < 0) {
                    if (abs(-diff.minus(parameters.amountSl.minus(nativeAmount))) < parameters.available) {
                        val addAmount = nativeAmount.minus(parameters.amount).minus(diff)
                        if (addAmount >= 0) {
                            throw AddAmountException(
                                "Amount should be add \$%.2f".format(kotlin.math.abs(addAmount)),
                                addAmount
                            )
                        }
                        throw RefundException(
                            "Order should be refund \$%.2f".format(kotlin.math.abs(addAmount)),
                            addAmount
                        )
                    } else {
                        throw AvailableNotEnoughException("Available not enough.")
                    }
                } else {
                    val refund = nativeAmount.minus(parameters.amount)
                    throw RefundException(
                        "Order should be refund \$%.2f".format(abs(refund)),
                        refund
                    )
                }
            }
        }
    }
}