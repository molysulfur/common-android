package com.awonar.android.shared.domain.marketprofile

import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketProfileRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinancialInfoUseCase @Inject constructor(
    private val repository: MarketProfileRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Int, FinancialResponse?>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<FinancialResponse?>> =
        repository.getFinancialInfo(parameters)
}