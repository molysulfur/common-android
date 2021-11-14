package com.awonar.android.shared.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.awonar.android.model.history.History
import com.awonar.android.model.history.HistoryResponse
import com.awonar.android.shared.api.HistoryService
import java.sql.Timestamp
import java.util.*
import javax.inject.Inject

class HistoryPagingSource @Inject constructor(
    private val service: HistoryService,
    private val timestamp: Long,
) : PagingSource<Int, History>() {
    override fun getRefreshKey(state: PagingState<Int, History>): Int? {
        return state.anchorPosition?.let { postition ->
            val page = state.closestPageToPosition(postition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, History> {
        val page = params.key ?: 1
        return try {
            val response =
                service.getHistory(
                    page = page,
                    limit = 10,
                    startDate = timestamp
                )
            response.histories?.forEachIndexed { index, history ->
                val master = response.masters?.find { it.id == history.masterId }
                master.also { response.histories?.get(index)?.master = it }
            }
            LoadResult.Page(
                response.histories ?: emptyList(),
                prevKey = null,
                nextKey = if (response.meta.hasMore) page + 1 else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}