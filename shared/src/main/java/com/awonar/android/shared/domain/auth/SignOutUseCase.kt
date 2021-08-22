package com.awonar.android.shared.domain.auth

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.AuthRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) :
    FlowUseCase<Unit, Boolean>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Boolean>> = flow {
        repository.signOut().collect { result ->
            when (result) {
                is Result.Success -> emit(Result.Success(result.data?.accessToken == null))
                is Result.Error -> emit(Result.Error(result.exception))
                is Result.Loading -> emit(Result.Loading)
            }
        }
    }
}