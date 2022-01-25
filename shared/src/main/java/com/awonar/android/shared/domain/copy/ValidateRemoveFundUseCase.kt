package com.awonar.android.shared.domain.copy

import com.awonar.android.exception.UpdateFundException
import com.awonar.android.model.copier.ValidateRemoveFundRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateRemoveFundUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRemoveFundRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateRemoveFundRequest) {
        val copy = parameters.copier
        val invested =
            copy.initialInvestment
                .plus(copy.closedPositionsNetProfit)
                .plus(copy.depositSummary)
                .minus(copy.withdrawalSummary)
                .minus(copy.availableAmount)

        val MAX_AMOUNT = if (invested >= 100) {
            copy.availableAmount
        } else {
            copy.initialInvestment
                .plus(copy.closedPositionsNetProfit)
                .plus(copy.depositSummary)
                .minus(copy.withdrawalSummary)
                .minus(100)
        }
        val MIN_AMOUNT = 0f

        if (parameters.amount > MAX_AMOUNT) {
            throw UpdateFundException(
                "Max amount to remove : $%.2f".format(MAX_AMOUNT),
                MAX_AMOUNT
            )
        }
        if (parameters.amount < MIN_AMOUNT) {
            throw UpdateFundException(
                "Min amount to remove : $%.2f".format(MIN_AMOUNT),
                MIN_AMOUNT
            )
        }
    }
}