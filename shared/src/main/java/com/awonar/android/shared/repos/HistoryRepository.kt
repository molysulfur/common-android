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

    fun getMarketHistory(request: Long): Flow<PagingData<MarketHistory>> {
        TODO("")
    }
}