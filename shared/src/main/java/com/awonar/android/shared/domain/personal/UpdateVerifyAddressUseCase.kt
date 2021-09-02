package com.awonar.android.shared.domain.personal

import com.awonar.android.model.privacy.PersonalAddressRequest
import com.awonar.android.model.privacy.PersonalProfileRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateVerifyAddressUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<PersonalAddressRequest, PersonalInfoResponse?>(ioDispatcher) {
    override fun execute(parameters: PersonalAddressRequest): Flow<Result<PersonalInfoResponse?>> =
        userRepository.verifyAddress(parameters)
}