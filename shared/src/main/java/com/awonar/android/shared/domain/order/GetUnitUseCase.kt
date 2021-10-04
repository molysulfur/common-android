package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.CalAmountUnitRequest
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetUnitUseCase @Inject constructor(
    private val currencyRepository: CurrenciesRepository,
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<CalAmountUnitRequest, Int>(dispatcher) {
    override suspend fun execute(parameters: CalAmountUnitRequest): Int {
        val conversionRate =
            currencyRepository.getConversionByInstrumentId(instrumentId = parameters.instrumentId)
        return parameters.amount.times(conversionRate.rateAsk).times(parameters.leverage)
            .div(parameters.price).toInt()
    }
}