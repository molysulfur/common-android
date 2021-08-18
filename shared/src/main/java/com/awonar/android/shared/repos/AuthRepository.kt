package com.awonar.android.shared.repos

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.api.AuthService
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    fun signInWithPassword(request: SignInRequest): Flow<Result<Auth?>> = flow {
        Timber.e("AuthRepository")
        val response = authService.signInWithPassword(request).execute()
        Timber.e("$response")
        emit(Result.Success(response.body()))
    }


}