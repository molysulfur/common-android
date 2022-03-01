package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPublicPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Position>, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        parameters.forEachIndexed { index, position ->
            val conversionRate =
                currenciesRepository.getConversionByInstrumentId(position.instrument?.id
                    ?: 0).rateBid
            itemList.add(PortfolioItem.InstrumentPortfolioItem(
                position = position,
                conversionRate = conversionRate,
                meta = DateUtils.getDate(position.openDateTime),
                index = index
            ))
        }
        return itemList
    }

}
