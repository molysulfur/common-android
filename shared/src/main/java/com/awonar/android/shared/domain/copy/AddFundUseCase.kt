package com.awonar.android.shared.domain.copy

import com.awonar.android.model.copier.AddFundRequest
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.SocialTradeRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFundUseCase @Inject constructor(
    private val repository: SocialTradeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<AddFundRequest, Copier?>(dispatcher) {
    override fun execute(parameters: AddFundRequest): Flow<Result<Copier?>> =
        repository.addFund(parameters)
}