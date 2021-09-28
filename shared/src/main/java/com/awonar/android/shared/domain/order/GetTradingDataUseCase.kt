package com.awonar.android.shared.domain.order

import com.awonar.android.shared.db.room.TradingData
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTradingDataUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<TradingData>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<TradingData>>> =
        repository.getTradingData(true)
}