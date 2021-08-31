package com.awonar.android.shared.domain.personal

import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPersonalInfoUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, PersonalInfoResponse?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<PersonalInfoResponse?>> =
        flow {
            repository.getPersonalInfo().collect {
                val user = it.successOr(null)
                emit(Result.Success(user))
            }
        }

}