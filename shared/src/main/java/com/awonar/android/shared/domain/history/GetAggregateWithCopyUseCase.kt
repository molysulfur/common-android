package com.awonar.android.shared.domain.history

import com.awonar.android.model.history.CopiesHistory
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAggregateWithCopyUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) :FlowUseCase<String, CopiesHistory?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<CopiesHistory?>> = repository.getAggregateWithCopiesId(parameters)
}