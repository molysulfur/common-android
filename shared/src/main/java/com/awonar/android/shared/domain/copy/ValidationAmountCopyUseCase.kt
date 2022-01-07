package com.awonar.android.shared.domain.copy

import com.awonar.android.exception.ValidationAmountCopyException
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ValidationAmountCopyUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Float, Unit>(dispatcher) {
    override suspend fun execute(parameters: Float) {
        portfolioRepository.getPortFolio().collect {
            val allocate: Float = it.successOr(null)?.totalAllocated ?: 0f
            val MAX_AMOUNT = 100000f.minus(allocate)
            val MIN_AMOUNT = 100f
            if (parameters > MAX_AMOUNT) {
                throw ValidationAmountCopyException(
                    "Max Amount is %.2f".format(100000f.minus(allocate)),
                    MAX_AMOUNT
                )
            }
            if (parameters < MIN_AMOUNT) {
                throw ValidationAmountCopyException("Min Amount is %.2f".format(100f), MIN_AMOUNT)
            }
        }
    }
}