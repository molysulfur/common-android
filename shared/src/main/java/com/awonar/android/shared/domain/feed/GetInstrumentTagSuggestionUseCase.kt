package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.Tag
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInstrumentTagSuggestionUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, List<Tag>?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<Tag>?>> =
        repository.findInstrumentTags(parameters)
}
