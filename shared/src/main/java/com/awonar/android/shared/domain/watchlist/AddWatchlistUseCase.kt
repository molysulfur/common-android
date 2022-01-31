package com.awonar.android.shared.domain.watchlist

import com.awonar.android.model.watchlist.AddWatchlistItemRequest
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
import javax.inject.Inject

class AddWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<AddWatchlistItemRequest, List<Folder>>(dispatcher) {
    override fun execute(parameters: AddWatchlistItemRequest): Flow<Result<List<Folder>>> = flow {
        repository.addWatchlist(parameters).collect {
            if (it.succeeded) {
                emit(Result.Success(it.successOr(emptyList()) ?: emptyList()))
            }
            if (it is Result.Error) {
                emit(it)
            }
        }
    }
}