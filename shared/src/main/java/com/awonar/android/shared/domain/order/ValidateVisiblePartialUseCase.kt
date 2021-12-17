package com.awonar.android.shared.domain.order

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateVisiblePartialUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<HasPartialRequest, Boolean>(dispatcher) {
    override suspend fun execute(parameters: HasPartialRequest): Boolean {
        val amount = parameters.amount
        val leverage = parameters.leverage
        val trading = repository.getTradingDataById(parameters.id)
        val minPosition =
            if (leverage < trading.minLeverage) trading.minPositionExposure.div(leverage) else trading.minPositionAmount
        return (amount > minPosition) && (amount.minus(minPosition) > minPosition)
    }
}

data class HasPartialRequest(
    val amount: Float,
    val leverage: Int,
    val id: Int
)