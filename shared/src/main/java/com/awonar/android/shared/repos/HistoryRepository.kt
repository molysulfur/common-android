package com.awonar.android.shared.repos

import androidx.paging.*
import com.awonar.android.model.history.*
import com.awonar.android.shared.api.HistoryService
import com.awonar.android.shared.constrant.Columns.COLUMNS_HISTORY
import com.awonar.android.shared.constrant.Columns.DEFAULT_COLUMN_HISTORY
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    val historyService: HistoryService,
    private val preference: PortfolioActivedColumnManager
) {

    fun getHistory(request: HistoryRequest): Flow<Result<HistoryPaging?>> =
        object : DirectNetworkFlow<Long, HistoryPaging, HistoryResponse>() {
            override fun createCall(): Response<HistoryResponse> =
                historyService.getHistory(startDate = request.timestamp, page = request.page)
                    .execute()

            override fun convertToResultType(response: HistoryResponse): HistoryPaging {
                response.histories?.forEach { history ->
                    val master = response.masters?.find { it.id == history.masterId }
                    history.master = master
                }

                return HistoryPaging(
                    response.histories ?: emptyList(),
                    if (response.meta.hasMore) response.meta.page.plus(1) else 0
                )
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getAggregate(parameters: Long): Flow<Result<Aggregate?>> =
        object : DirectNetworkFlow<Long, Aggregate, Aggregate>() {
            override fun createCall(): Response<Aggregate> =
                historyService.getAggregate(parameters).execute()

            override fun convertToResultType(response: Aggregate): Aggregate = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getActivedColumn(): List<String> = preference.getHistory() ?: DEFAULT_COLUMN_HISTORY

    fun getColumnsList(actived: List<String>): List<String> {
        val columns = COLUMNS_HISTORY
        return columns.filter { !actived.contains(it) }
    }

    fun updateActivedColumn(columns: List<String>) {
        preference.saveHistoryColumn(columns)
    }

    fun resetColumn(): List<String> {
        preference.clearHistory()
        return getActivedColumn()
    }

    fun getMarketHistory(request: HistoryRequest): Flow<Result<MarketHistoryPaging?>> =
        object : DirectNetworkFlow<Long, MarketHistoryPaging, MarketHistoryResponse>() {
            override fun createCall(): Response<MarketHistoryResponse> =
                historyService.getMarketHistory(page = request.page, startDate = request.timestamp)
                    .execute()

            override fun convertToResultType(response: MarketHistoryResponse): MarketHistoryPaging {
                val page = if (response.meta.hasMore) response.meta.page + 1 else 0
                return MarketHistoryPaging(response.markets, page)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun filterMarketHistory(request: HistoryRequest) =
        object : DirectNetworkFlow<Long, MarketHistoryPaging, MarketHistoryResponse>() {
            override fun createCall(): Response<MarketHistoryResponse> =
                historyService.filterMarketHistory(
                    page = request.page,
                    startDate = request.timestamp,
                    symbol = request.symbol ?: "",
                    filter = request.filter ?: ""
                ).execute()

            override fun convertToResultType(response: MarketHistoryResponse): MarketHistoryPaging {
                val newPage = if (response.meta.hasMore) response.meta.page + 1 else 0
                return MarketHistoryPaging(response.markets, newPage)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }


        }.asFlow()

    fun filterHistory(request: HistoryRequest) =
        object : DirectNetworkFlow<Long, HistoryPaging, HistoryResponse>() {
            override fun createCall(): Response<HistoryResponse> =
                historyService.filterHistory(
                    page = request.page,
                    startDate = request.timestamp,
                    symbol = request.symbol ?: "",
                    filter = request.filter ?: ""
                ).execute()

            override fun convertToResultType(response: HistoryResponse): HistoryPaging {
                val newPage = if (response.meta.hasMore) response.meta.page + 1 else 0
                return HistoryPaging(response.histories ?: emptyList(), newPage)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }


        }.asFlow()

    fun getCopiesHistory(
        username: String,
        timestamp: Long
    ): Flow<Result<CopiesAggregateResponse?>> =
        object : DirectNetworkFlow<Long, CopiesAggregateResponse?, CopiesAggregateResponse?>() {
            override fun createCall(): Response<CopiesAggregateResponse?> =
                historyService.getCopiesHistory(
                    username = username,
                    startDate = timestamp
                ).execute()

            override fun convertToResultType(response: CopiesAggregateResponse?): CopiesAggregateResponse? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }


        }.asFlow()
}