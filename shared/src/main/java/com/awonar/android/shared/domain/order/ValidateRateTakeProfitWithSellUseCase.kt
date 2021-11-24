package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateRateTakeProfitRequest
import com.awonar.android.shared.di.IoDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateRateTakeProfitWithSellUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRateTakeProfitRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: ValidateRateTakeProfitRequest): Price {
//        val slRate = parameters.takeProfit.unit
//        val minRate = parameters.openPrice
//        if (slRate > minRate) {
//            throw ValidateStopLossException("Take Profit cannot less than $minRate", minRate)
//        }
//        return parameters.takeProfit
        TODO("")
    }
}