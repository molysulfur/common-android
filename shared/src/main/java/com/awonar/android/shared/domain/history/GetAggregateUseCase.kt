package com.awonar.android.shared.domain.history

import com.awonar.android.model.history.Aggregate
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAggregateUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, Aggregate?>(dispatcher) {
    override fun execute(parameters: Long): Flow<Result<Aggregate?>> =
        repository.getAggregate(parameters)
}