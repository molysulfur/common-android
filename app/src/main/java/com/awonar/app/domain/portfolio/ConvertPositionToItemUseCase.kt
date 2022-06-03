package com.awonar.app.domain.portfolio

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject


class ConvertPositionToItemUseCase @Inject constructor(
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertInsidePosition, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertInsidePosition): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        val conversionRate = parameters.currentItem.conversionRate
        val positions = parameters.portfolio.positions
        val instrumentId = parameters.currentItem.instrumentGroup?.get(0)?.instrument?.id
        positions?.filter { it.instrumentId == instrumentId }?.forEach {
            itemList.add(
                PortfolioItem.PositionItem(
                    positionId = it.instrumentId,
                    instrumentGroup = arrayListOf(it),
                    conversionRate = conversionRate
                )
            )
        }
        return itemList
    }

    private suspend fun calculateAmount(
        instrumentId: Int,
        rate: Float,
        open: Float,
        unit: Float,
        isBuy: Boolean,
    ): Float {
        val request = StopLossRequest(
            instrumentId = instrumentId,
            stopLoss = Price(0f, rate, "amount"),
            openPrice = open,
            unit = unit
        )
        val result = if (isBuy) {
            calculateAmountStopLossAndTakeProfitWithBuyUseCase(request)
        } else {
            calculateAmountStopLossAndTakeProfitWithSellUseCase(request)
        }
        if (result.succeeded) {
            return result.data?.amount ?: 0f
        }
        return 0f
    }
}

data class ConvertInsidePosition(
    val portfolio: UserPortfolioResponse,
    val currentItem: PortfolioItem.PositionItem,
)
