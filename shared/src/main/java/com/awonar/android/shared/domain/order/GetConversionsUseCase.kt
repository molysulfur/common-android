package com.awonar.android.shared.domain.order

import com.awonar.android.model.Conversion
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConversionsUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) :
    FlowUseCase<Unit, List<Conversion>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<Conversion>>> =
        repository.getConversion(true)
}