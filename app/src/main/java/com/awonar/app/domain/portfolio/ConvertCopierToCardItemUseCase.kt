package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertCopierToCardItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Copier>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Copier>): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        parameters.forEach { copier ->
            val invested = copier.initialInvestment
            val moneyInMoneyOut = copier.depositSummary - copier.withdrawalSummary
            itemList.add(
                OrderPortfolioItem.CopierPositionCardItem(
                    copier = copier,
                    invested = invested,
                    profitLoss = 0f,
                    money = moneyInMoneyOut,
                    value = 0f
                )
            )
        }
        return itemList
    }

}