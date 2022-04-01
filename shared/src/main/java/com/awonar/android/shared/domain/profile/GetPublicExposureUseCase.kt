package com.awonar.android.shared.domain.profile

import com.awonar.android.model.profile.PublicExposureRequest
import com.awonar.android.model.profile.PublicExposure
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.ProfileRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPublicExposureUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<PublicExposureRequest, List<PublicExposure>?>(dispatcher) {
    override fun execute(parameters: PublicExposureRequest): Flow<Result<List<PublicExposure>?>> =
        profileRepository.getPublicExposure(parameters)
}