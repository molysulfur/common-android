package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.repos.PortfolioRepository
import com.awonar.android.shared.utils.PortfolioUtil
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class GetPieChartAllocateUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    private val currenciesRepository: CurrenciesRepository,
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Map<String, Double>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Map<String, Double>>> = flow {

        portfolioRepository.getUserPortfolio().collect { result ->
            val position = result.successOr(null)
            val portfolio = portfolioRepository.getPortFolio().last().successOr(null)
            val totalAmount: Double =
                portfolio?.totalAllocated?.plus(portfolio.available)?.toDouble() ?: 0.0
            val balance: Double = portfolio?.available?.toDouble() ?: 0.0
            var totalPL = 0f
            val market: Double = position?.positions?.sumOf { position ->
                var marketPL = 0f
                getProfitLoss(position).collect {
                    marketPL = marketPL.plus(it)
                }
                totalPL = totalPL.plus(marketPL)
                position.amount.toDouble() + marketPL
            } ?: 0.0
            val people = position?.copies?.sumOf { copier ->
                var peoplePL = 0f
                copier.positions?.forEach { position ->
                    getProfitLoss(position).collect {
                        peoplePL = peoplePL.plus(it)
                    }
                }
                totalPL = totalPL.plus(peoplePL)
                copier.initialInvestment.plus(copier.depositSummary.minus(copier.withdrawalSummary))
                    .plus(peoplePL).toDouble()
            } ?: 0.0
            val allocate = HashMap<String, Double>()
            allocate["balance"] = balance.div(totalAmount + totalPL).times(100)
            allocate["market"] = market.div(totalAmount + totalPL).times(100)
            allocate["People"] = people.div(totalAmount + totalPL).times(100)
            emit(Result.Success(allocate))
        }
    }

    private fun getProfitLoss(position: Position): Flow<Float> = flow {
        val conversion = currenciesRepository.getConversionByInstrumentId(position.instrumentId)
        marketRepository.getLastPriceWithId(position.instrumentId).collect {
            if (it.succeeded) {
                val current = if (position.isBuy) it.data?.bid else it.data?.ask
                val pl = PortfolioUtil.getProfitOrLoss(
                    current ?: 0f,
                    position.openRate,
                    position.units,
                    conversion.rateBid,
                    position.isBuy
                )
                Timber.e("getProfitLoss $pl")
                emit(pl)
            }
        }
    }
}