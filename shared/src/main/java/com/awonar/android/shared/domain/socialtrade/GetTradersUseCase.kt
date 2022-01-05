package com.awonar.android.shared.domain.socialtrade

import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SocialTradeRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTradersUseCase @Inject constructor(
    private val repository: SocialTradeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<TradersRequest, List<Trader>?>(dispatcher) {
    override fun execute(parameters: TradersRequest): Flow<Result<List<Trader>?>> =
        repository.getTraders(parameters)
}