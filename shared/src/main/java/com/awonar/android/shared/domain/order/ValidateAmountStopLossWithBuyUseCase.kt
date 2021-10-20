package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ValidateAmountStopLossWithBuyUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateStopLossRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: ValidateStopLossRequest): Price {
        val tradingData = repository.getTradingDataById(parameters.instrumentId)
        val slRate = parameters.stopLoss.amount
        val maxAmountSL =
            -(parameters.amount.times(tradingData.maxStopLossPercentageLeveragedBuy)
                .div(100))
        Timber.e("$slRate $maxAmountSL ${tradingData.maxStopLossPercentageLeveragedBuy}")
        if (slRate < maxAmountSL) {
            throw ValidateStopLossException("Stop loss cannot less than $maxAmountSL", maxAmountSL)
        }
        return parameters.stopLoss
    }
}