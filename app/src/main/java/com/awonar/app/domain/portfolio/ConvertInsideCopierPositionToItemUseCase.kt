package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Copier
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertInsideCopierPositionToItemUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Copier, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: Copier): List<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        val positions = parameters.positions
        positions?.forEach {
            val instrument = repository.getConversionByInstrumentId(it.instrument?.id ?: 0)
            itemList.add(
                PortfolioItem.PositionItem(
                    positionId = it.instrumentId,
                    instrumentGroup = arrayListOf(it),
                    conversionRate = instrument.rateBid
                )
            )
        }
        return itemList
    }

}