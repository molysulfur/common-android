package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.DefaultStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetDefaultStopLossWithLeverageUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<DefaultStopLossRequest, Float>(dispatcher) {
    override suspend fun execute(parameters: DefaultStopLossRequest): Float {
        val trading = repository.getTradingDataById(parameters.instrumentId)
        return parameters.amount.times(trading.defaultStopLossPercentageLeveraged.div(100))
    }
}