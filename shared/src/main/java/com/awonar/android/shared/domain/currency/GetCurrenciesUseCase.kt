package com.awonar.android.shared.domain.currency

import com.awonar.android.model.currency.Currency
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String,Currency>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Currency>> {
        TODO("Not yet implemented")
    }


}