package com.awonar.android.shared.domain.history

import com.awonar.android.model.history.CopiesAggregateResponse
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCopiesHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<HistoryRequest, CopiesAggregateResponse?>(dispatcher) {
    override fun execute(parameters: HistoryRequest): Flow<Result<CopiesAggregateResponse?>> =
        repository.getCopiesHistory(parameters.username ?: "", parameters.timestamp)
}