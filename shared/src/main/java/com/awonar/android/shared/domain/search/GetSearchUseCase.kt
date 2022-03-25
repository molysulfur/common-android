package com.awonar.android.shared.domain.search

import com.awonar.android.model.search.SearchRequest
import com.awonar.android.model.search.SearchResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SearchRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequest, SearchResponse?>(dispatcher) {
    override fun execute(parameters: SearchRequest): Flow<Result<SearchResponse?>> =
        repository.getSearch(parameters)
}