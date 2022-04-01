package com.awonar.android.shared.domain.watchlist

import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.WatchlistRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistFoldersUseCase @Inject constructor(
    private val repository: WatchlistRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Boolean, List<Folder>>(dispatcher) {
    override fun execute(parameters: Boolean): Flow<Result<List<Folder>>> =
        repository.getFolders(parameters)
}