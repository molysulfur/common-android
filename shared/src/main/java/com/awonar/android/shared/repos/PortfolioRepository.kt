package com.awonar.android.shared.repos

import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.api.PortfolioService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioService: PortfolioService
) {

    fun getPortFolio() = object : DirectNetworkFlow<Boolean, Portfolio, Portfolio>() {
        override fun createCall(): Response<Portfolio> = portfolioService.getPortFolio().execute()

        override fun convertToResultType(response: Portfolio): Portfolio = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()
}
