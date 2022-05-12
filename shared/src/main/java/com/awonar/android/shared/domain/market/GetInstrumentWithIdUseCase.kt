package com.awonar.android.shared.domain.market

import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetInstrumentWithIdUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : UseCase<Int, Instrument?>(ioDispatcher) {
    override suspend fun execute(parameters: Int): Instrument? =
        repository.getInstrumentFromDao(parameters)
}