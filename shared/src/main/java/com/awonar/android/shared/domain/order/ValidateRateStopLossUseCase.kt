package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.ValidateRateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.pow

class ValidateRateStopLossUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRateStopLossRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateRateStopLossRequest) {
        val minRateSl = if (parameters.isBuy) parameters.quote.bid else parameters.quote.ask
        val maxRateSL = 10f.pow(-parameters.digit)
        val slRate = parameters.rateSl
        if (parameters.isBuy) {
            if ((slRate < maxRateSL) or (slRate > minRateSl)) {
                throw ValidationException("Stop Loss cannot less than $minRateSl", minRateSl)
            }
        } else if (slRate < minRateSl) {
            throw ValidationException("Stop Loss cannot less than $minRateSl", minRateSl)
        }
    }
}


//                val diff = amountSl.minus(maxAmountSl)
//                if (diff < 0) {
//                    if (kotlin.math.abs(-diff.minus(parameters.amountSl.minus(nativeAmount))) < parameters.available) {
//                        val addAmount = nativeAmount.minus(parameters.amount).minus(diff)
//                        Timber.e("$nativeAmount ${parameters.amount} ${parameters.available}")
//                        when{
//                            addAmount > 0 -> throw AddAmountException(
//                                "Amount should be add \$%.2f".format(kotlin.math.abs(addAmount)),
//                                addAmount
//                            )
//                            addAmount < 0 ->  throw RefundException(
//                                "Order should be refund \$%.2f".format(kotlin.math.abs(addAmount)),
//                                addAmount
//                            )
//                        }
//                    } else {
//                        throw AvailableNotEnoughException("Available not enough.")
//                    }
//                } else {
//                    val refund = nativeAmount.minus(parameters.amount)
//                    if(refund > 0) {
//                        throw RefundException(
//                            "Order should be refund \$%.2f".format(kotlin.math.abs(refund)),
//                            refund
//                        )
//                    }
//                }
