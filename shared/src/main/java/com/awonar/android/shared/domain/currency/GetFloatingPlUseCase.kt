package com.awonar.android.shared.domain.currency

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.PortfolioUtil
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetFloatingPlUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Position>, Float>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): Float {
        val sumPL = 0f
        parameters.forEach { position ->
            val quote = QuoteSteamingManager.quotesState.value[position.instrument.id]
            quote?.let {
                val current = if (position.isBuy) it.bid else it.ask
                val pl = PortfolioUtil.getProfitOrLoss(
                    current,
                    position.openRate,
                    position.units,
                    repository.getConversionByInstrumentId(position.instrument.id).rateBid,
                    position.isBuy
                )
                sumPL.plus(pl)
            }
        }
        return sumPL
    }
}