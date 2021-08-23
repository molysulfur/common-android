package com.awonar.android.shared.repos

import com.awonar.android.model.*
import com.awonar.android.shared.api.AuthService
import com.awonar.android.shared.db.hawk.AccessTokenManager
import com.molysulfur.library.network.DirectNetworkFlow
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
                println(errorMessage)
            }

            override fun shouldFresh(data: Auth?): Boolean = true

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

    fun autoSignIn() = object : NetworkFlow<Unit, Auth?, Auth?>() {
        override fun shouldFresh(data: Auth?): Boolean = data == null || data.isExpireToken()

        override fun createCall(): Response<Auth?> =
            authService.getRefreshToken(accessTokenManager.load()).execute()

        override fun convertToResultType(response: Auth?): Auth? = response

        override fun loadFromDb(): Flow<Auth?> = flow {
            emit(accessTokenManager.load())
        }

        override fun saveToDb(data: Auth?) {
            if (data != null) {
                accessTokenManager.save(data)
            }
        }

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun forgotPassword() {

    }

    fun signInWithGoogle(request: SignInGoogleRequest): Flow<Result<Auth?>> =
        object : NetworkFlow<SignInRequest, Auth?, Auth?>() {
            override fun createCall(): Response<Auth?> {
                return authService.signInWithGoogle(request).execute()
            }

            override fun convertToResultType(response: Auth?): Auth? = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

            override fun shouldFresh(data: Auth?): Boolean = true

            override fun loadFromDb(): Flow<Auth?> = flow {
                emit(accessTokenManager.load())
            }

            override fun saveToDb(data: Auth?) {
                if (data != null) {
                    accessTokenManager.save(data)
                }
            }

        }.asFlow()

    fun signOut(): Flow<Result<Auth?>> = object : NetworkFlow<Unit, Auth?, SignOutResponse?>() {
        override fun shouldFresh(data: Auth?): Boolean = true

        override fun createCall(): Response<SignOutResponse?> = authService.signOut().execute()

        override fun convertToResultType(response: SignOutResponse?): Auth? = null

        override fun loadFromDb(): Flow<Auth?> = flow {
            emit(accessTokenManager.load())
        }

        override fun saveToDb(data: Auth?) {
            accessTokenManager.clearToken()
        }

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun signInWithFacebook(request: SignInFacebookRequest): Flow<Result<Auth?>> =
        object : NetworkFlow<SignInRequest, Auth?, Auth?>() {
            override fun createCall(): Response<Auth?> {
                return authService.signInWithGoogle(request).execute()
            }

            override fun convertToResultType(response: Auth?): Auth? = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

            override fun shouldFresh(data: Auth?): Boolean = true

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