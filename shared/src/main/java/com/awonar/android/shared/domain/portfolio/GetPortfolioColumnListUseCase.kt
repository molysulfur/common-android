package com.awonar.android.shared.domain.portfolio

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPortfolioColumnListUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<String>, List<String>>(dispatcher) {
    override suspend fun execute(parameters: List<String>): List<String> =
        repository.getPortfolioManualColumns(parameters)
}