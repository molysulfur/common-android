package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPositionToCardItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Position>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        parameters.forEach {
            val conversion: Float =
                currenciesRepository.getConversionByInstrumentId(it.instrumentId).rateBid
            itemList.add(
                OrderPortfolioItem.InstrumentPositionCardItem(
                    position = it,
                    conversion = conversion,
                    units = it.units,
                    avgOpen = it.openRate,
                    invested = it.amount,
                    profitLoss = 0f,
                    value = 0f,
                    leverage = it.leverage.toFloat(),
                    current = 0f
                )
            )
        }
        return itemList
    }

}