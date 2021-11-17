package com.awonar.android.shared.domain.history

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awonar.android.model.history.MarketHistory
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetMarketHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Long, PagingData<MarketHistory>>(dispatcher) {

    override fun execute(parameters: Long): Flow<Result<PagingData<MarketHistory>>> = flow {
        repository.getMarketHistory(parameters)
            .cachedIn(CoroutineScope(context = currentCoroutineContext()))
            .collect {
                emit(Result.Success(it))
            }
    }
}