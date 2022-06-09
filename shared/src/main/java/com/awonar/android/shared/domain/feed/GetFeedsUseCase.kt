package com.awonar.android.shared.domain.feed

import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.model.feed.FeedRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.FeedRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetFeedsUseCase @Inject constructor(
    private val repository: FeedRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<FeedRequest, FeedPaging?>(dispatcher) {

    override fun execute(parameters: FeedRequest): Flow<Result<FeedPaging?>> = flow {
        val flow = if (parameters.prefixPath.isNotBlank()) {
            repository.getAllFeed(parameters.type, parameters.prefixPath, parameters.page)
        } else {
            repository.getAllFeed(parameters.type, parameters.page)
        }
        flow.collectIndexed { _, result ->
            val data = result.successOr(null)
            coroutineScope {
                val metas = data?.feeds?.mapIndexed { _, feed ->
                    when {
                        feed?.type == "news" -> {
                            val deffer = async {
                                repository.getMetaNews(feed.links?.get(0) ?: "")
                            }
                            val meta = deffer.await().last().successOr(null)
                            feed.meta = meta
                            feed
                        }
                        feed?.sharePostData?.type == "news" -> {
                            val deffer = async {
                                repository.getMetaNews(feed.sharePostData?.links?.get(0) ?: "")
                            }
                            val meta = deffer.await().last().successOr(null)
                            feed.sharePostData?.meta = meta
                            feed
                        }
                        else -> {
                            feed
                        }
                    }
                }
                emit(Result.Success(FeedPaging(metas ?: mutableListOf(), data?.page ?: 0)))
            }
        }
    }
}