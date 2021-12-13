package com.awonar.android.shared.domain.currency

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesRateUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Float>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Float>> = flow<Result<Float>> {
        repository.getCurrencyRate(parameters).collect {
            val rate: Float = it.successOr(0f) ?: 0f
            emit(Result.Success(rate))
        }
    }
}