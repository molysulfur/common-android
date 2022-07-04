package com.awonar.android.shared.domain.order

import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.ValidateRateTakeProfitRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.pow


class ValidateRateTakeProfitUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ValidateRateTakeProfitRequest, Unit>(dispatcher) {

    override suspend fun execute(parameters: ValidateRateTakeProfitRequest) {
        val rateTp = parameters.rateTp
        val MIN_RATE_TP = if(parameters.isBuy) parameters.quote.bid else parameters.quote.ask
        /**
         * Check MIN RATE
         */
        when {
            parameters.isBuy && (rateTp < MIN_RATE_TP) -> {
                throw ValidationException(
                    "Take Profit can't less than $MIN_RATE_TP",
                    MIN_RATE_TP
                )
            }
            !parameters.isBuy && (rateTp > MIN_RATE_TP) -> {
                throw ValidationException(
                    "Take Profit can't more than $MIN_RATE_TP",
                    MIN_RATE_TP
                )
            }
            else -> {}
        }
        /**
         * Check Max Rate
         */
        val pl = PortfolioUtil.getProfitOrLoss(
            current = parameters.currentPrice,
            openRate = parameters.openPrice,
            unit = parameters.units,
            conversionRate = 1f,
            isBuy = parameters.isBuy
        )
        val value = pl.plus(parameters.amount)
        val MAX_AMOUNT_TP = value.times(parameters.maxTakeProfitPercentage).div(100)
        when (parameters.isBuy) {
            true -> {
                val MAX_RATE_TP = ConverterOrderUtil.convertAmountToRate(
                    MAX_AMOUNT_TP,
                    parameters.conversionRate,
                    parameters.units,
                    parameters.openPrice,
                    parameters.isBuy
                )
                Timber.e("$rateTp , $MAX_RATE_TP")
                if (rateTp > MAX_RATE_TP) {
                    throw ValidationException(
                        "Take Profit can't more than $MAX_RATE_TP",
                        MAX_RATE_TP
                    )
                }
            }
            false -> {
                val MAX_RATE_TP = 10f.pow(-parameters.digit)
                if (rateTp <= MAX_RATE_TP) {
                    throw ValidationException(
                        "Take Profit can't less than $MAX_RATE_TP",
                        MAX_RATE_TP
                    )
                }
            }
        }
    }

}