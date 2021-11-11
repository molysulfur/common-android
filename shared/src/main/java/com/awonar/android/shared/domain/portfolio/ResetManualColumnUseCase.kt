package com.awonar.android.shared.domain.portfolio

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ResetManualColumnUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(parameters: Unit) {
        repository.clearActivedManualColumn()
    }
}