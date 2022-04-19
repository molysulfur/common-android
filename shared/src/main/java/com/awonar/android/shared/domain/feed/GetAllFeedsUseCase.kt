package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.model.feed.NewsMeta
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.FeedRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.wait
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class GetAllFeedsUseCase @Inject constructor(
    private val repository: FeedRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Int, FeedPaging?>(dispatcher) {

    override fun execute(parameters: Int): Flow<Result<FeedPaging?>> = flow {
        Timber.e("$parameters")
        repository.getAllFeed(parameters).collectIndexed { _, result ->
            val data = result.successOr(null)
            coroutineScope {
                val metas = data?.feeds?.mapIndexed { index, feed ->
                    if (feed?.type == "news") {
                        val deffer = async {
                            repository.getMetaNews(feed.newsId ?: "")
                        }
                        val meta = deffer.await().last().successOr(null)
                        feed.meta = meta
                        feed
                    } else {
                        feed
                    }
                }
                emit(Result.Success(FeedPaging(metas?: mutableListOf(),data?.page?:0)))
            }
        }
    }
}