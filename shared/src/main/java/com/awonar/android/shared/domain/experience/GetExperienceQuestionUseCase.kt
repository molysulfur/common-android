package com.awonar.android.shared.domain.experience

import com.awonar.android.model.experience.ExperienceResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExperienceQuestionUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ExperienceResponse?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<ExperienceResponse?>> =
        repository.getExperience()
}