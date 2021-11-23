package com.awonar.android.shared.domain.history

import com.awonar.android.model.history.HistoryPaging
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<HistoryRequest,HistoryPaging?>(dispatcher) {
    override fun execute(parameters: HistoryRequest): Flow<Result<HistoryPaging?>> =
        historyRepository.getHistory(parameters)
}