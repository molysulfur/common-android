package com.awonar.android.shared.domain.order

import com.awonar.android.exception.RateException
import com.awonar.android.model.order.ValidateRateRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlin.math.pow

class ValidateMaxRateUseCase @Inject constructor(@MainDispatcher dispatcher: CoroutineDispatcher) :
    UseCase<ValidateRateRequest, Boolean>(dispatcher) {

    companion object {
        private const val MAX_RATE = 1000
    }

    override suspend fun execute(parameters: ValidateRateRequest): Boolean {
        val maxRate: Float = parameters.currentRate.times(MAX_RATE)
        val maxString = "%.${parameters.digit}f".format(maxRate)
        if (parameters.rate >= maxRate) {
            throw RateException("Rate must no more than $maxString", maxString.toFloat())
        }
        return true
    }


}