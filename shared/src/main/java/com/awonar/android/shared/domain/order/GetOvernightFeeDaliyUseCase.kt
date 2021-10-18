package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.OvernightFeeRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class GetOvernightFeeDaliyUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<OvernightFeeRequest, Float>(dispatcher) {
    override suspend fun execute(parameters: OvernightFeeRequest): Float {
        val amount = parameters.amount
        val trading = repository.getTradingDataById(parameters.instrumentId)
        val overnightFee = when {
            parameters.leverage > 1 && parameters.orderType == "buy" -> trading.leveragedBuyOverNightFee
            parameters.leverage > 1 && parameters.orderType == "sell" -> trading.leveragedSellOverNightFee
            parameters.leverage == 1 && parameters.orderType == "buy" -> trading.nonLeveragedBuyOverNightFee
            parameters.leverage == 1 && parameters.orderType == "sell" -> trading.nonLeveragedSellOverNightFee
            else -> 0f
        }
        return overnightFee.times(amount.unit)
    }
}