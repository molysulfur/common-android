package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.CreateFeed
import com.awonar.android.model.feed.Feed
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.FeedRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateFeedUseCase @Inject constructor(
    private val repository: FeedRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<CreateFeed, Feed?>(dispatcher) {
    override fun execute(parameters: CreateFeed): Flow<Result<Feed?>> =
        repository.create(parameters)
}