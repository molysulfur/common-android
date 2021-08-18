package com.awonar.android.shared.repos

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.api.AuthService
import com.molysulfur.library.network.FlowNetwork
import com.molysulfur.library.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    fun signInWithPassword(request: SignInRequest): Flow<Result<Auth?>> {
        return object : FlowNetwork<SignInRequest, Auth?, Auth?>() {
            override fun createCall(): Response<Auth?> {
                return authService.signInWithPassword(request).execute()
            }

            override fun convertToResultType(response: Auth?): Auth? = response

            override fun onFetchFailed(errorMessage: String) {
                Timber.e(errorMessage)
            }

        }.asFlow()
    }


}