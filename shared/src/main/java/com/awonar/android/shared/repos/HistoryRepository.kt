package com.awonar.android.shared.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.History
import com.awonar.android.shared.api.HistoryService
import com.awonar.android.shared.constrant.Columns.COLUMNS_HISTORY
import com.awonar.android.shared.constrant.Columns.DEFAULT_COLUMN_HISTORY
import com.awonar.android.shared.data.HistoryPagingSource
import com.awonar.android.shared.db.hawk.PortfolioActivedColumnManager
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val historyService: HistoryService,
    private val preference: PortfolioActivedColumnManager
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    fun getHistory(request: Long): Flow<PagingData<History>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                HistoryPagingSource(
                    historyService,
                    request
                )
            }
        ).flow
    }


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
}