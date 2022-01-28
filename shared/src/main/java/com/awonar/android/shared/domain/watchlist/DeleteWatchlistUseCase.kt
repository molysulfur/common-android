package com.awonar.android.shared.domain.watchlist

import com.awonar.android.model.watchlist.DeleteWatchlistRequest
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.WatchlistRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DeleteWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<DeleteWatchlistRequest, List<Folder>>(dispatcher) {
    override fun execute(parameters: DeleteWatchlistRequest): Flow<Result<List<Folder>>> = flow {
        repository.deleteWatchlist(parameters.folderId,parameters.itemId).collect {
            Timber.e("$it")
            if (it.succeeded) {
                emit(Result.Success(it.successOr(emptyList()) ?: emptyList()))
            }
            if (it is Result.Error) {
                emit(it)
            }
        }
    }
}