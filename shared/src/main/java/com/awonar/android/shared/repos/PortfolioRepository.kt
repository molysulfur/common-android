package com.awonar.android.shared.repos

import com.awonar.android.constrant.manualColumns
import com.awonar.android.constrant.marketColumns
import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.api.PortfolioService
import com.awonar.android.shared.constrant.Columns.DEFAULT_COLUMN_MARKET
import com.awonar.android.shared.constrant.Columns.DEFAULT_COLUMN_POSITION
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class PortfolioRepository @Inject constructor(
    private val portfolioService: PortfolioService,
    private val preference: PortfolioActivedColumnManager,
) {

    fun getPositionPublic(request: PublicPositionRequest) =
        object : DirectNetworkFlow<PublicPositionRequest, PublicPosition?, PublicPosition?>() {
            override fun createCall(): Response<PublicPosition?> =
                portfolioService.getPublicPosition(request.username, request.symbol).execute()

            override fun convertToResultType(response: PublicPosition?): PublicPosition? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getPendingOrders() =
        object : DirectNetworkFlow<Unit, List<PendingOrder>, List<PendingOrder>>() {
            override fun createCall(): Response<List<PendingOrder>> =
                portfolioService.getPendingOrders().execute()

            override fun convertToResultType(response: List<PendingOrder>): List<PendingOrder> =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()


    fun getMyCopier() = object : DirectNetworkFlow<Unit, List<Copier>, List<Copier>>() {
        override fun createCall(): Response<List<Copier>> =
            portfolioService.getMyCopier().execute()

        override fun convertToResultType(response: List<Copier>): List<Copier> = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }
    }.asFlow()

    fun getMyPositions() = object : DirectNetworkFlow<Unit, List<Position>, List<Position>>() {
        override fun createCall(): Response<List<Position>> =
            portfolioService.getMyPositions().execute()

        override fun convertToResultType(response: List<Position>): List<Position> = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }
    }.asFlow()

    fun getPortFolio() = object : DirectNetworkFlow<Boolean, Portfolio, UserPortfolioResponse>() {
        override fun createCall(): Response<UserPortfolioResponse> = portfolioService.getMyPositionManual().execute()

        override fun convertToResultType(response: UserPortfolioResponse): Portfolio = response.portfolio

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun getUserPortfolio(username: String) =
        object : DirectNetworkFlow<Boolean, UserPortfolioResponse, UserPortfolioResponse>() {
            override fun createCall(): Response<UserPortfolioResponse> =
                portfolioService.getUserPositionManual(username).execute()

            override fun convertToResultType(response: UserPortfolioResponse): UserPortfolioResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getUserPortfolio() =
        object : DirectNetworkFlow<Boolean, UserPortfolioResponse, UserPortfolioResponse>() {
            override fun createCall(): Response<UserPortfolioResponse> =
                portfolioService.getMyPositionManual().execute()

            override fun convertToResultType(response: UserPortfolioResponse): UserPortfolioResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getActivedManualColumn(): List<String> =
        preference.getManual() ?: DEFAULT_COLUMN_POSITION

    fun getActivedMarketColumn(): List<String> =
        preference.getMarket() ?: DEFAULT_COLUMN_MARKET


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
