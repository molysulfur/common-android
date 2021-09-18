package com.awonar.android.shared.domain.marketprofile

import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketProfileUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Int, InstrumentProfile?>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<InstrumentProfile?>> =
        repository.getInstrument(parameters)
}