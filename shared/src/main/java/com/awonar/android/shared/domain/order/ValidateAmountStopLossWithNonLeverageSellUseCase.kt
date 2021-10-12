package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateAmountStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.pow

class ValidateAmountStopLossWithNonLeverageSellUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateAmountStopLossRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: ValidateAmountStopLossRequest): Price {
        val tradingData = repository.getTradingDataById(parameters.instrumentId)
        val slRate = parameters.stopLoss.amount
        val maxAmountSL =
            -(parameters.stopLoss.amount.times(tradingData.maxStopLossPercentageNonLeveragedSell)
                .div(100))

        if (slRate < maxAmountSL) {
            throw ValidateStopLossException("Stop loss cannot less than $maxAmountSL",maxAmountSL)
        }
        return parameters.stopLoss
    }
}