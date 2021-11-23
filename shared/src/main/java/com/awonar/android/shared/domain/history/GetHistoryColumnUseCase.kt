package com.awonar.android.shared.domain.history

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.HistoryRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetHistoryColumnUseCase @Inject constructor(
    private val repository: HistoryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase< List<String>, List<String>>(dispatcher) {
    override suspend fun execute(parameters:  List<String>): List<String> = repository.getColumnsList(parameters)
}