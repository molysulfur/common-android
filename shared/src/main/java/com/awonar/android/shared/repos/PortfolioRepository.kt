package com.awonar.android.shared.repos

import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.api.PortfolioService
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioService: PortfolioService,
    private val portfolioActivedColumnManager: PortfolioActivedColumnManager
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
        portfolioActivedColumnManager.get() ?: listOf(
            "invested",
            "execute at",
            "current",
            "pip chnage"
        )

    fun clearActivedColumn() {
        portfolioActivedColumnManager.clear()
    }
}
