package com.awonar.android.shared.domain.copy

import com.awonar.android.shared.di.MainDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * @param Pair<Float,Float>
 *     first is amount
 *     secound is StopLoss Amount
 */
class GetRatioStopLossUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Pair<Float, Float>, Float>(dispatcher) {
    override suspend fun execute(parameters: Pair<Float, Float>): Float =
        parameters.second.div(parameters.first)
}