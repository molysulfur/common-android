package com.awonar.android.shared.domain.order

import com.awonar.android.exception.MinRateException
import com.awonar.android.model.order.ValidateMinRateRequest
import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ValidateMinRateUseCase @Inject constructor(@MainDispatcher dispatcher: CoroutineDispatcher) :
    UseCase<ValidateMinRateRequest, Boolean>(dispatcher) {

    companion object {
        private const val MIN_RATE = 10
    }

    override suspend fun execute(parameters: ValidateMinRateRequest): Boolean {
        val minRate: Float = parameters.currentRate.div(MIN_RATE)
        if (parameters.rate <= minRate) {
            return throw MinRateException("Rate must not lower than $minRate", minRate)
        }
        return true
    }


}