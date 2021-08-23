package com.awonar.android.shared.domain.auth

import com.awonar.android.exception.UnAuthenticationException
import com.awonar.android.exception.UnAuthenticationIsExistEmailException
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInGoogleRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.AuthRepository
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignInGoogleRequest, Auth?>(ioDispatcher) {
    override fun execute(parameters: SignInGoogleRequest): Flow<Result<Auth?>> = flow {
        repository.signInWithGoogle(parameters).catch { e ->
            this.emit(Result.Error(UnAuthenticationException(e.message)))
        }.collect { result ->

            when (result) {
                is Result.Success -> {
                    this.emit(Result.Success(result.data))
                }
                is Result.Error -> {
                    checkEmailExist(parameters, this)
                }
                is Result.Loading -> {
                    this.emit(Result.Loading)
                }
            }
        }
    }

    private suspend fun checkEmailExist(
        parameters: SignInGoogleRequest,
        flowCollector: FlowCollector<Result<Auth?>>
    ) {
        userRepository.checkEmailExist(parameters.email).collect { hasEmailResult ->
            when (hasEmailResult) {
                is Result.Success -> {
                    val hasEmail: Boolean = hasEmailResult.data ?: false
                    if (hasEmail) {
                        flowCollector.emit(Result.Error(UnAuthenticationIsExistEmailException("Email not already linked account.")))
                    }
                    flowCollector.emit(Result.Error(UnAuthenticationException("Email is not registry.")))
                }
                is Result.Error -> {
                    flowCollector.emit(Result.Error(UnAuthenticationIsExistEmailException("Email is not registry.")))
                }
            }


        }

    }


}