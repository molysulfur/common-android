package com.awonar.android.shared.domain.copy

import com.awonar.android.model.copier.CopiesRequest
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SocialTradeRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCopyUseCase @Inject constructor(
    private val repository: SocialTradeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<CopiesRequest, Copier?>(dispatcher) {
    override fun execute(parameters: CopiesRequest): Flow<Result<Copier?>> =
        repository.createCopy(parameters)
}