package com.awonar.android.shared.domain.user

import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<UserRequest, User>(ioDispatcher) {
    override fun execute(parameters: UserRequest): Flow<Result<User>> =
        flow {
            repository.getUser(parameters)
        }

}