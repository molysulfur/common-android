package com.awonar.android.shared.domain.copy

import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

/***
 * @param Pair<Float,Float>
 *     first is amount
 *     secound is StopLoss Amount
 */
class GetRatioStopLossUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Pair<Float, Float>, Float>(dispatcher) {
    override suspend fun execute(parameters: Pair<Float, Float>): Float {
        Timber.e("$parameters ${parameters.second.div(parameters.first)}")
        return parameters.second.div(parameters.first)
    }
}