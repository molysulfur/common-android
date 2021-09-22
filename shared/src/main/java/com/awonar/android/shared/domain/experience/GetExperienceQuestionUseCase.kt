package com.awonar.android.shared.domain.experience

import com.awonar.android.model.experience.ExperienceAnswerResponse
import com.awonar.android.model.experience.ExperienceResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetExperienceQuestionUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, ExperienceResponse?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<ExperienceResponse?>> = flow {
        repository.getExperience().collect {
            var exp = it.successOr(null)
            if (exp != null) {
                repository.getUserAnswerExperience("${exp.id}").collect { result ->
                    if (result.succeeded) {
                        exp = mapAnswerInQuestion(exp, (result as Result.Success).data)
                    }
                    Timber.e("$exp")
                    this.emit(Result.Success(exp))
                }
            } else {
                this.emit(Result.Success(exp))
            }
        }
    }

    private fun mapAnswerInQuestion(
        exp: ExperienceResponse?,
        answerExp: ExperienceAnswerResponse?
    ): ExperienceResponse? {
        if (exp != null && answerExp != null) {
            answerExp.topic.forEachIndexed { indexTopic, topicAnswer ->
                topicAnswer?.questions?.forEachIndexed { questionIndex, questionAnswer ->
                    exp.topics?.get(indexTopic)?.questions?.get(questionIndex)?.questionAnswer =
                        questionAnswer
                }
            }
        }
        return exp
    }
}