package com.awonar.android.shared.domain.profile

import com.awonar.android.model.user.StatTradeResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.ProfileRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTradingStatisticUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, StatTradeResponse?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<StatTradeResponse?>> =
        repository.getStatTrade(parameters)
}