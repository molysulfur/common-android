package com.awonar.android.shared.domain.order

import com.awonar.android.exception.RateException
import com.awonar.android.model.order.ValidateRateRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ValidateRateUseCase @Inject constructor(@MainDispatcher dispatcher: CoroutineDispatcher) :
    UseCase<ValidateRateRequest, Unit>(dispatcher) {

    companion object {
        private const val MIN_RATE = 10
        private const val MAX_RATE = 1000
    }

    override suspend fun execute(parameters: ValidateRateRequest) {
        val minRate: Float = parameters.currentRate.div(MIN_RATE)
        val minString = "%.${parameters.digit}f".format(minRate)
        val maxRate: Float = parameters.currentRate.times(MAX_RATE)
        val maxString = "%.${parameters.digit}f".format(maxRate)
        if (parameters.rate < minRate) {
            throw RateException("Rate must not lower than $minString", minString.toFloat())
        }
        if (parameters.rate >= maxRate) {
            throw RateException("Rate must no more than $maxString", maxString.toFloat())
        }
    }


}