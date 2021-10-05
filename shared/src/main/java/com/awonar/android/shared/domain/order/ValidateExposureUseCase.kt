package com.awonar.android.shared.domain.order

import com.awonar.android.exception.PositionExposureException
import com.awonar.android.model.order.ExposureRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ValidateExposureUseCase @Inject constructor(
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ExposureRequest, Boolean>(dispatcher) {
    override suspend fun execute(parameters: ExposureRequest): Boolean {
        val trading = marketRepository.getTradingDataById(parameters.instrumentId)
        val exposure = parameters.amount.times(parameters.leverage)
        val minPositionExposure = if (parameters.leverage <= trading.minLeverage) {
            trading.minPositionExposure
        } else {
            trading.minPositionAmount
        }
        val maxPositionExposure = if (parameters.leverage <= trading.minLeverage) {
            trading.maxPositionExposure
        } else {
            trading.maxPositionAmount
        }
        Timber.e("$exposure $minPositionExposure $maxPositionExposure")
        if (exposure > maxPositionExposure) {
            throw PositionExposureException(
                "Maximun position size is $%.2f".format(
                    maxPositionExposure.toFloat()
                )
            )
        }
        if (exposure < minPositionExposure) {
            throw PositionExposureException(
                "Minimun position size is $%.2f".format(
                    minPositionExposure.toFloat()
                )
            )
        }
        return true
    }
}