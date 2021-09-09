package com.awonar.android.shared.domain.experience

import com.awonar.android.model.experience.ExperienceAnswerResponse
import com.awonar.android.model.experience.ExperienceRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateQuestionnaireUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<ExperienceRequest, ExperienceAnswerResponse>(ioDispatcher) {
    override fun execute(parameters: ExperienceRequest): Flow<Result<ExperienceAnswerResponse>> =
        flow {
            repository.updateExperience(parameters).collect {

            }
        }
}