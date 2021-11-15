package com.awonar.android.shared.domain.history

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awonar.android.model.history.History
import com.awonar.android.shared.di.ApplicationScope
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, PagingData<History>>(dispatcher) {
    override fun execute(parameters: Long): Flow<Result<PagingData<History>>> = flow {
        historyRepository.getHistory(parameters)
            .cachedIn(CoroutineScope(coroutineContext))
            .collect {
                emit(Result.Success(it))
            }
    }
}