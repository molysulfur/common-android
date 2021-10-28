package com.awonar.android.shared.repos

import com.awonar.android.constrant.manualColumns
import com.awonar.android.constrant.marketColumns
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.api.PortfolioService
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioService: PortfolioService,
    private val preference: PortfolioActivedColumnManager
) {

    fun getPortFolio() = object : DirectNetworkFlow<Boolean, Portfolio, Portfolio>() {
        override fun createCall(): Response<Portfolio> = portfolioService.getPortFolio().execute()

        override fun convertToResultType(response: Portfolio): Portfolio = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun getUserPortfolio() =
        object : DirectNetworkFlow<Boolean, UserPortfolioResponse, UserPortfolioResponse>() {
            override fun createCall(): Response<UserPortfolioResponse> =
                portfolioService.getUserPortFolio().execute()

            override fun convertToResultType(response: UserPortfolioResponse): UserPortfolioResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getActivedManualColumn(): List<String> =
        preference.getManual() ?: listOf(
            "Invested",
            "Execute at",
            "Current",
            "Pip Change"
        )

    fun getActivedMarketColumn(): List<String> =
        preference.getManual() ?: listOf(
            "Units",
            "Avg. Open",
            "Invested",
            "P/L($)"
        )


    fun getPortfolioMarketColumns(activedList: List<String>): List<String> {
        return marketColumns.filter { it !in activedList }
    }

    fun getPortfolioManualColumns(activedList: List<String>): List<String> {
        return manualColumns.filter { it !in activedList }
    }

    fun updateManualColumn(newColumns: List<String>) {
        preference.saveManualColumn(newColumns)
    }

    fun updateMarketColumn(newColumns: List<String>) {
        preference.saveMarketColumn(newColumns)
    }

    fun clearActivedManualColumn() {
        preference.clearManual()
    }

    fun clearActivedMarketColumn() {
        preference.clearMarket()
    }

}
