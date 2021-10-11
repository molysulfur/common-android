package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject
import kotlin.math.pow

class ValidateStopLossUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateStopLossRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: ValidateStopLossRequest): Price {
        val slRate = parameters.stopLoss.unit
        val minRate = parameters.openPrice
        val maxRate = BigDecimal(10.0.pow(-parameters.digit))
            .setScale(
                parameters.digit,
                RoundingMode.DOWN
            )
        if (slRate > minRate) {
            throw ValidateStopLossException("Stop loss cannot more than $minRate")
        }
        if (BigDecimal("$slRate") < maxRate) {
            throw ValidateStopLossException("Stop loss cannot less than $maxRate")
        }

        return parameters.stopLoss
    }
}