package com.awonar.android.shared.domain.history

import com.awonar.android.model.history.History
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCopiesInsideHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, List<History>>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<History>>> {
        TODO("Not yet implemented")
    }
}