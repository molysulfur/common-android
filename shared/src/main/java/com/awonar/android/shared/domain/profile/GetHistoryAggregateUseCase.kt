package com.awonar.android.shared.domain.profile

import com.awonar.android.model.portfolio.HistoryPositionRequest
import com.awonar.android.model.portfolio.PublicHistoryAggregate
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryAggregateUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<HistoryPositionRequest, PublicHistoryAggregate?>(dispatcher) {
    override fun execute(parameters: HistoryPositionRequest): Flow<Result<PublicHistoryAggregate?>> =
        repository.getHistoryAggregate(parameters)
}