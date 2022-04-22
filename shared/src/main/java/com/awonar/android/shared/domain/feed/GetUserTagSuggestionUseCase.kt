package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.UserTag
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserTagSuggestionUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, List<UserTag>?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<UserTag>?>> =
        repository.getUserTagSuggestions(parameters)
}