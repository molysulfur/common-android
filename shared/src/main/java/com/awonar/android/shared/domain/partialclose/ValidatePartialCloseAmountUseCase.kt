package com.awonar.android.shared.domain.partialclose

import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.ValidatePartialAmountRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidatePartialCloseAmountUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ValidatePartialAmountRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: ValidatePartialAmountRequest) {
        val tradingData = repository.getTradingDataById(parameters.id)
        val pl = parameters.pl
        val leverage = parameters.leverage
        val amountReduct = parameters.inputAmount
        val value = parameters.amount.plus(pl)
        val unit = parameters.units
        val unitReduct = amountReduct.div(value).times(unit)
        val minAmount =
            if (leverage < tradingData.minLeverage) tradingData.minPositionExposure.div(parameters.leverage) else tradingData.minPositionAmount
        val realAmountReduct = unitReduct.div(unit).times(parameters.amount)
        if (amountReduct < minAmount) {
            throw ValidationException(
                "Close Amount can't be lower than \$%.2f".format(minAmount.toFloat()),
                minAmount.toFloat()
            )
        }
        if (amountReduct > value.minus(minAmount)) {
            throw ValidationException(
                "Close Amount can't be more than \$%.2f".format(value.minus(minAmount.toFloat())),
                value.minus(minAmount)
            )
        }
    }
}