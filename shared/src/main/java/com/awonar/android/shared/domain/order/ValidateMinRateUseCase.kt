package com.awonar.android.shared.domain.order

import com.awonar.android.exception.RateException
import com.awonar.android.model.order.ValidateRateRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ValidateMinRateUseCase @Inject constructor(@MainDispatcher dispatcher: CoroutineDispatcher) :
    UseCase<ValidateRateRequest, Boolean>(dispatcher) {

    companion object {
        private const val MIN_RATE = 10
    }

    override suspend fun execute(parameters: ValidateRateRequest): Boolean {
        val minRate: Float = parameters.currentRate.div(MIN_RATE)
        val minString = "%.${parameters.digit}f".format(minRate)
        if (parameters.rate < minRate) {
            return throw RateException("Rate must not lower than $minString", minString.toFloat())
        }
        return true
    }


}