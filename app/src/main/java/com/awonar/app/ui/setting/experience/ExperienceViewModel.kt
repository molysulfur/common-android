package com.awonar.app.ui.setting.experience

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.experience.*
import com.awonar.android.shared.domain.experience.GetExperienceQuestionUseCase
import com.awonar.android.shared.domain.experience.UpdateQuestionnaireUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import com.molysulfur.library.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getExperienceQuestionUseCase: GetExperienceQuestionUseCase,
    private val updateQuestionnaireUseCase: UpdateQuestionnaireUseCase
) : ViewModel() {

    val experienceAnswer: MutableStateFlow<ArrayList<ExperienceRequest?>> = MutableStateFlow(
        arrayListOf()
    )

    val experienceQuestion = getExperienceQuestionUseCase(Unit).map {
        it.data
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _experienceItem = MutableStateFlow<List<ExperienceItem?>>(emptyList())
    val experienceItem: StateFlow<List<ExperienceItem?>> get() = _experienceItem

    fun convertExperienceItem(
        title: String,
        subTitle: String,
        topic: Topic,
        isNoExpShow: Boolean = false
    ) {
        val itemList = arrayListOf<ExperienceItem>()
        if (title.isNotBlank()) {
            itemList.add(ExperienceItem.Title(title))
            itemList.add(ExperienceItem.Blank())
        }
        if (subTitle.isNotBlank()) {
            itemList.add(ExperienceItem.SubTitle(subTitle))
            itemList.add(ExperienceItem.Blank())
        }

        topic.questions?.forEach { question ->
            when (question?.type) {
                QuestionType.CHOICE -> {
                    if (question.multiple) {
                        itemList.add(ExperienceItem.Title(question.title ?: ""))
                        itemList.add(ExperienceItem.SubTitle(question.description ?: ""))
                        val questionList = ArrayList(question.questionOption)
                        if (isNoExpShow) {
                            questionList.add(
                                QuestionOption(
                                    null,
                                    null,
                                    "I do not have any knowledge of finance.",
                                    "0",
                                    "0",
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                        if (!question.id.isNullOrBlank() && !topic.id.isNullOrBlank()) {
                            itemList.add(
                                ExperienceItem.CheckBoxQuestion(
                                    questionId = question.id!!,
                                    topicId = topic.id!!,
                                    option = questionList
                                )
                            )
                        }

                    } else {
                        itemList.add(ExperienceItem.Title(question.title ?: ""))
                        itemList.add(ExperienceItem.SubTitle(question.description ?: ""))
                        val questionList = ArrayList(question.questionOption)
                        if (isNoExpShow) {
                            questionList.add(
                                QuestionOption(
                                    null,
                                    null,
                                    "I do not have any knowledge of finance.",
                                    "0",
                                    "0",
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                        if (!question.id.isNullOrBlank() && !topic.id.isNullOrBlank()) {
                            itemList.add(
                                ExperienceItem.RadioQuestion(
                                    questionId = question.id!!,
                                    topicId = topic.id!!,
                                    option = questionList
                                )
                            )
                        }

                    }
                }
            }

        }
        _experienceItem.value = itemList
    }

    fun addAnswer(questionId: String?, answer: Answer?) {
        Timber.e("$questionId $answer")
        if (answer != null) {
            experienceAnswer.value[0]?.questionAnswers?.forEach {
                if (questionId == it.questionId) {
                    it.answers.add(answer)
                }
            }
        }
    }

    fun createRequest(questionnaireId: String, data: Topic?, position: Int) {
        val questionAnswers = arrayListOf<QuestionAnswer>()
        data?.questions?.forEach { question ->
            questionAnswers.add(
                QuestionAnswer(
                    questionId = question?.id,
                    answers = arrayListOf()
                )
            )
        }

        experienceAnswer.value.add(
            ExperienceRequest(
                questionnaireId = questionnaireId,
                topicId = data?.id,
                questionAnswers = questionAnswers
            )
        )

    }

    fun submit() {
        viewModelScope.launch {
            experienceAnswer.value.let {
                it[0]?.let { it1 -> updateQuestionnaireUseCase(it1).collect {

                } }

            }
        }
    }

}