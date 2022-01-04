package com.awonar.android.shared.domain.socialtrade

import com.awonar.android.model.socialtrade.CopierRecommended
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SocialTradeRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendedUseCase @Inject constructor(
    private val repository: SocialTradeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<CopierRecommended>?>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<CopierRecommended>?>> =
        repository.getRecommended()

}