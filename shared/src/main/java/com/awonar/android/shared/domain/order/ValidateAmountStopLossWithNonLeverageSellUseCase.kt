package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateAmountStopLossWithNonLeverageSellUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateStopLossRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: ValidateStopLossRequest): Price {
        val tradingData = repository.getTradingDataById(parameters.instrumentId)
        val slRate = parameters.stopLoss.amount
        val maxAmountSL =
            -(parameters.stopLoss.amount.times(tradingData.maxStopLossPercentageNonLeveragedSell)
                .div(100))

        if (slRate < maxAmountSL) {
            throw ValidationException("Stop loss cannot less than $maxAmountSL", maxAmountSL)
        }
        return parameters.stopLoss
    }
}