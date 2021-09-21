package com.awonar.android.shared.domain.market

import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInstrumentListUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Instrument>?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<Instrument>?>> =
        repository.getInstrumentList()
}