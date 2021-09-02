package com.awonar.android.shared.domain.personal

import com.awonar.android.exception.VerifyPersonalException
import com.awonar.android.model.privacy.PersonalCardIdRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateVerifyCardIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<PersonalCardIdRequest, PersonalInfoResponse?>(ioDispatcher) {
    override fun execute(parameters: PersonalCardIdRequest): Flow<Result<PersonalInfoResponse?>> {
        if (parameters.idType < 0) {
            return throw  VerifyPersonalException("Choose you id type, please")
        }
        if (parameters.idType == 1) {
            if (parameters.idDocs[0].isNullOrBlank() && parameters.idDocs[1].isNullOrBlank()) {
                return throw  VerifyPersonalException("Choose you id type, please")
            }
        }
        return userRepository.verifyCardId(parameters)
    }

}