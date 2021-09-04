package com.awonar.android.shared.domain.remote

import com.awonar.android.model.settting.Country
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.RemoteConfigRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: RemoteConfigRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Country>?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<Country>?>> = flow {
        repository.getCountriesList().collect {
            val data = it.successOr(listOf())
            emit(Result.Success(data))
        }
    }
}