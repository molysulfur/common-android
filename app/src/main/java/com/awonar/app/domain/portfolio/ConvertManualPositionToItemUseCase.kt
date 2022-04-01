package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertManualPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<UserPortfolioResponse?, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: UserPortfolioResponse?): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        var sumInvested = 0f
        parameters?.positions?.forEachIndexed { index, position ->
            val conversionRate =
                currenciesRepository.getConversionByInstrumentId(position.instrumentId).rateBid
            sumInvested += position.invested
            itemList.add(PortfolioItem.InstrumentPortfolioItem(
                position = position,
                conversionRate = conversionRate,
                meta = null,
                index = index
            ))
        }
        parameters?.copies?.forEachIndexed { index, copier ->
            sumInvested += copier.invested
            itemList.add(PortfolioItem.CopierPortfolioItem(
                copier = copier,
                conversions = emptyMap(),
                index = index
            ))
        }
        itemList.add(PortfolioItem.BalanceItem(
            title = "Balance",
            value = 100f.minus(sumInvested)
        ))
        return itemList
    }

}