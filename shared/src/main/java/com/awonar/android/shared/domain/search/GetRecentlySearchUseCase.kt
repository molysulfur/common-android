package com.awonar.android.shared.domain.search

import com.awonar.android.model.search.Search
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SearchRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentlySearchUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<Search>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<Search>>> = flow {
        repository.getRecentlySearch().collect {
            val data: List<Search> = it.successOr(emptyList()) ?: emptyList()
            emit(Result.Success(data))
        }
    }
}