package com.awonar.android.shared.domain.copy

import com.awonar.android.exception.ValidationStopLossCopyException
import com.awonar.android.model.copier.ValidateCopyRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateStoplossCopyUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateCopyRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateCopyRequest) {
        val MAX_STOPLOSS = parameters.amount.times(0.95f)
        val MIN_STOPLOSS = parameters.amount.times(0.05f)
        if (parameters.stopLoss > MAX_STOPLOSS) {
            throw ValidationStopLossCopyException(
                "Min $%.2f, Max $%.2f".format(MIN_STOPLOSS, MAX_STOPLOSS),
                MAX_STOPLOSS
            )
        }
        if (parameters.stopLoss < MIN_STOPLOSS) {
            throw ValidationStopLossCopyException(
                "Min $%.2f, Max $%.2f".format(MIN_STOPLOSS, MAX_STOPLOSS),
                MIN_STOPLOSS
            )
        }
    }
}