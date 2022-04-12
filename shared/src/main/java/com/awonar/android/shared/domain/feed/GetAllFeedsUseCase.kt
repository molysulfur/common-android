package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.FeedRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFeedsUseCase @Inject constructor(
    private val repository: FeedRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Int, FeedPaging?>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<FeedPaging?>> =
        repository.getAllFeed(parameters)
}