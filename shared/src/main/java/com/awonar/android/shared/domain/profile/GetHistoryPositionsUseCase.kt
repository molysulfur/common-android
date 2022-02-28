package com.awonar.android.shared.domain.profile

import com.awonar.android.model.portfolio.HistoryPosition
import com.awonar.android.model.portfolio.HistoryPositionRequest
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHistoryPositionsUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<HistoryPositionRequest, List<HistoryPosition>>(dispatcher) {
    override fun execute(parameters: HistoryPositionRequest): Flow<Result<List<HistoryPosition>>> =
        flow {
            repository.getHistoryPositions(parameters).collect {
                val list = it.successOr(emptyList()) ?: emptyList()
                emit(Result.Success(list))
            }
        }
}