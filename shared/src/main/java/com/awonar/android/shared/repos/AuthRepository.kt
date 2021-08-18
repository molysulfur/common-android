package com.awonar.android.shared.repos

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.api.AuthService
import com.awonar.android.shared.db.hawk.AccessTokenManager
import com.molysulfur.library.network.NetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val accessTokenManager: AccessTokenManager
) {
    fun signInWithPassword(request: SignInRequest): Flow<Result<Auth?>> {
        return object : NetworkFlow<SignInRequest, Auth?, Auth?>() {
            override fun createCall(): Response<Auth?> {
                return authService.signInWithPassword(request).execute()
            }

            override fun convertToResultType(response: Auth?): Auth? = response

            override fun onFetchFailed(errorMessage: String) {
                Timber.e(errorMessage)
            }

            override fun shouldFresh(data: Auth?): Boolean {
                Timber.e("${data == null} || ${data?.isExpireToken()}")
                return data == null || data.isExpireToken()
            }

            override fun loadFromDb(): Flow<Auth?> = flow {
                emit(accessTokenManager.load())
            }

            override fun saveToDb(data: Auth?) {
                if (data != null) {
                    accessTokenManager.save(data)
                }
            }

        }.asFlow()
    }


}