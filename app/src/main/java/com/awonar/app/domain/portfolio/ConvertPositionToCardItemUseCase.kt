package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPositionToCardItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Position>, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): List<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        parameters.forEach { position ->
            val conversion: Float =
                currenciesRepository.getConversionByInstrumentId(position.instrumentId).rateBid
            with(position) {
                invested = position.amount
            }
            itemList.add(
                PortfolioItem.InstrumentPositionCardItem(
                    position = position,
                    conversion = conversion,

                    )
            )
        }
        return itemList
    }

}