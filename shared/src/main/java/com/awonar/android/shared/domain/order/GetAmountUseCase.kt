package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.CalAmountUnitRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class GetAmountUseCase @Inject constructor(
    private val currencyRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<CalAmountUnitRequest, Float>(dispatcher) {
    override suspend fun execute(parameters: CalAmountUnitRequest): Float {
        val conversionRate =
            currencyRepository.getConversionByInstrumentId(instrumentId = parameters.instrumentId)
        if (conversionRate != null) {
            Timber.e(
                """$parameters"""
            )
            return parameters.amount.times(parameters.price).div(parameters.leverage)
                .div(conversionRate.rateAsk)
        }
        throw NullPointerException()
    }
}

