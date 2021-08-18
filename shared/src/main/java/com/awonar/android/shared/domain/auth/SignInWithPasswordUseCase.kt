package com.awonar.android.shared.domain.auth

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.AuthRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithPasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignInRequest, Auth?>(ioDispatcher) {
    override fun execute(parameters: SignInRequest): Flow<Result<Auth?>> =
        repository.signInWithPassword(SignInRequest("", ""))


}