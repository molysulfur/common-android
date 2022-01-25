package com.awonar.android.shared.domain.copy

import com.awonar.android.exception.ValidationStopLossCopyException
import com.awonar.android.model.copier.ValidateEditStopLossCopyRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateEditStopLossCopyUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ValidateEditStopLossCopyRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidateEditStopLossCopyRequest) {
        val MAX_STOPLOSS = parameters.netInvest.plus(parameters.pl).times(0.99f)
        val MIN_STOPLOSS = parameters.netInvest.times(0.05f)
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