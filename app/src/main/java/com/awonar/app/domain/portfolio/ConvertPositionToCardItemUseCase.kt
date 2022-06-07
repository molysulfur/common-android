package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ConvertPositionToCardItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<UserPortfolioResponse, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: UserPortfolioResponse): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        parameters.positions?.forEach { position ->
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
        Timber.e("$itemList")
        return itemList
    }

}