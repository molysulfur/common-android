package com.awonar.android.shared.domain.order

import com.awonar.android.exception.PositionExposureException
import com.awonar.android.model.order.ExposureRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateExposureUseCase @Inject constructor(
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ExposureRequest, Boolean>(dispatcher) {
    override suspend fun execute(parameters: ExposureRequest): Boolean {
        val trading = marketRepository.getTradingDataById(parameters.instrumentId)
        val exposure =
            if (parameters.leverage < trading.minLeverage) parameters.amount.times(parameters.leverage) else parameters.amount
        val leverage = if (parameters.leverage < trading.minLeverage) parameters.leverage else 1
        var minPositionExposure = 1
        var maxPositionExposure = 1
        if (parameters.leverage < trading.minLeverage) {
            minPositionExposure = trading.minPositionExposure
            maxPositionExposure = trading.maxPositionExposure

        } else {
            minPositionExposure = trading.minPositionAmount
            maxPositionExposure = trading.maxPositionAmount
        }
        if (exposure > maxPositionExposure) {
            val maximun = maxPositionExposure.div(leverage).toFloat()
            throw PositionExposureException(
                "Maximun position size is $%.2f".format(maximun), maximun
            )
        }
        if (exposure < minPositionExposure) {
            val minimum = minPositionExposure.div(leverage).toFloat()
            throw PositionExposureException(
                "Minimun position size is $%.2f".format(minimum), minimum
            )
        }
        return true
    }
}