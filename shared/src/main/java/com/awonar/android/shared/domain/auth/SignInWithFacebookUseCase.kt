package com.awonar.android.shared.domain.auth

import com.awonar.android.exception.UnAuthenticationException
import com.awonar.android.exception.UnAuthenticationIsExistEmailException
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInFacebookRequest
import com.awonar.android.model.SignInGoogleRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.AuthRepository
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class SignInWithFacebookUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignInFacebookRequest, Auth?>(ioDispatcher) {
    override fun execute(parameters: SignInFacebookRequest): Flow<Result<Auth?>> = flow {
        repository.signInWithFacebook(parameters).catch { e ->
            this.emit(Result.Error(UnAuthenticationException(e.message)))
        }.collect { result ->

            when (result) {
                is Result.Success -> {
                    this.emit(Result.Success(result.data))
                }
                is Result.Error -> {
                    checkEmailExist(this)
                }
                is Result.Loading -> {
                    this.emit(Result.Loading)
                }
            }
        }
    }

    private suspend fun checkEmailExist(
        flowCollector: FlowCollector<Result<Auth?>>
    ) {

        val facebookUserInfo = userRepository.getUserInfoFromFacebook()
        val email = facebookUserInfo.getJSONObject()?.getString("email")
        userRepository.checkEmailExist(email)
            .collect { hasEmailResult ->
                when (hasEmailResult) {
                    is Result.Success -> {
                        val hasEmail: Boolean = hasEmailResult.data ?: false
                        if (hasEmail) {
                            flowCollector.emit(
                                Result.Error(
                                    UnAuthenticationIsExistEmailException(
                                        "Email not already linked account.",
                                        email
                                    )
                                )
                            )
                        }
                        flowCollector.emit(Result.Error(UnAuthenticationException("Email is not registry.")))
                    }
                    is Result.Error -> {
                        flowCollector.emit(
                            Result.Error(
                                UnAuthenticationIsExistEmailException(
                                    "Email is not registry.",
                                    email
                                )
                            )
                        )
                    }
                }


            }

    }


}