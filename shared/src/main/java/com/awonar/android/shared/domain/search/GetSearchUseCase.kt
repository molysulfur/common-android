package com.awonar.android.shared.domain.search

import com.awonar.android.model.search.SearchRequest
import com.awonar.android.model.search.SearchResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SearchRepository
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import timber.log.Timber
import javax.inject.Inject

class GetSearchUseCase @Inject constructor(
    private val repository: SearchRepository,
    private val userRepository: UserRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequest, SearchResponse?>(dispatcher) {
    override fun execute(parameters: SearchRequest): Flow<Result<SearchResponse?>> = flow {
        repository.getSearch(parameters).collect { result ->
            val data = result.successOr(null)
            data?.people?.map { user ->
                user.data?.apply {
                    isFollow =
                        userRepository.isFollowing(user.data?.id ?: "", true).last()
                            .successOr(false)
                            ?: false
                }
            }
            emit(Result.Success(data))
        }
    }
}