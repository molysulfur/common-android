package com.awonar.android.shared.domain.history

import androidx.paging.PagingData
import com.awonar.android.model.history.History
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, PagingData<History>>(dispatcher) {
    override fun execute(parameters: Long): Flow<Result<PagingData<History>>> = flow {
        historyRepository.getHistory(parameters).collect {
            emit(Result.Success(it))
        }
    }
}