package com.awonar.android.shared.domain.auth

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.AuthRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : UseCase<String, String>(ioDispatcher) {
    override suspend fun execute(parameters: String): String {
        repository.forgotPassword()
        return "We send email to mo************@g******** to complete the verification email process. You have to click on the link inside."
    }
}