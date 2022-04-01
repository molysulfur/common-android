package com.awonar.android.shared.domain.search

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SearchRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearRecentlyUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Unit?>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Unit?>> = repository.clearRecently()

}