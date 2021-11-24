package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.ValidateRateTakeProfitRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlin.math.pow


class ValidateRateTakeProfitUseCase @Inject constructor(
    private val repository: MarketRepository,
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidateRateTakeProfitRequest, Unit>(dispatcher) {

    override suspend fun execute(parameters: ValidateRateTakeProfitRequest) {
        val trading = repository.getTradingDataById(parameters.instrument.id)
        val conversionRate =
            currenciesRepository.getConversionByInstrumentId(parameters.instrument.id).rateBid
        val maxAmountTp = parameters.value.times(trading.maxTakeProfitPercentage).div(100)
        val SELL_MAX_RATE = 10f.pow(-parameters.instrument.digit)
        val BUY_MAX_RATE =
            maxAmountTp.times(conversionRate).div(parameters.units).plus(parameters.openPrice)
        val minRate = parameters.currentPrice
        val maxRate = if (parameters.isBuy) BUY_MAX_RATE else SELL_MAX_RATE
        val tpRate = parameters.rateTp
        val amountTp =
            tpRate.minus(parameters.openPrice).times(parameters.units).div(conversionRate)
        if (parameters.isBuy) {
            validateBuy(tpRate, minRate, maxAmountTp, amountTp)
        } else {
            validateSell(tpRate, minRate, amountTp, maxAmountTp, maxRate)
        }
    }

    private fun validateSell(
        tpRate: Float,
        minRate: Float,
        amountTp: Float,
        maxAmountTp: Float,
        maxRate: Float
    ) {
        if (tpRate > minRate) {
            throw ValidationException("Take Profit cannot more than $minRate", minRate)
        } else {
            if (amountTp > maxAmountTp) {
                throw ValidationException("Take Profit cannot more than $minRate", minRate)
            }
            if (tpRate < maxRate) {
                throw ValidationException("Take Profit cannot less than $minRate", minRate)
            }
        }
    }

    private fun validateBuy(
        tpRate: Float,
        minRate: Float,
        maxAmountTp: Float,
        amountTp: Float
    ) {
        if (tpRate < minRate) {
            throw ValidationException("Take Profit cannot less than $minRate", minRate)
        } else {
            if (amountTp > maxAmountTp) {
                throw ValidationException("Take Profit cannot more than $maxAmountTp", maxAmountTp)
            }
        }
    }


}