package com.awonar.android.shared.repos

import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.api.PortfolioService
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import timber.log.Timber
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

    fun getActivedColumn(): List<String> =
        preference.get() ?: listOf(
            "Invested",
            "Execute at",
            "Current",
            "Pip Change"
        )

    fun clearActivedColumn() {
        preference.clear()
    }

    fun getPortfolioMarketColumns(activedList: List<String>): List<String> {
        val columnList = listOf(
            "Invested",
            "Units",
            "Avg. Open",
            "Current",
            "S/L($)",
            "S/L(%)",
            "Leverage",
            "Value",
            "Fee",
            "Net Invest",
            "CSL",
            "CSL(%)"
        )
        return columnList.filter { it !in activedList }
    }

    fun getPortfolioManualColumns(activedList: List<String>): List<String> {
        val columnList = listOf(
            "Invested",
            "Units",
            "Open",
            "Current",
            "S/L($)",
            "S/L(%)",
            "Pip Change",
            "Leverage",
            "Value",
            "Fee",
            "Execute at",
            "SL",
            "TP",
            "SL($)",
            "TP($)",
            "SL(%)",
            "TP(%)"
        )
        return columnList.filter { it !in activedList }
    }

    fun updateColumn(newColumns: List<String>) {
        preference.save(newColumns)
    }
}
