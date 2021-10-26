package com.awonar.android.shared.domain.market

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetConversionByInstrumentUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Int, Float>(dispatcher) {
    override suspend fun execute(parameters: Int): Float =
        repository.getConversionByInstrumentId(parameters).rateBid
}