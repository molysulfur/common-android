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

    private val _financialSourceIncomeState = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val financialSourceIncomeState: StateFlow<ArrayList<String>> get() = _financialSourceIncomeState

    private val _currentPageState = MutableStateFlow(0)

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
                    convertChoiceType(question, itemList, isNoExpShow, topic)
                }
            }

        }
        _experienceItem.value = itemList
    }

    private fun convertChoiceType(
        question: Question,
        itemList: ArrayList<ExperienceItem>,
        isNoExpShow: Boolean,
        topic: Topic
    ) {
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

    fun addAnswer(questionId: String?, answer: Answer?) {
        if (answer != null) {
            experienceAnswer.value[_currentPageState.value]?.questionAnswers?.forEach {
                if (questionId == it.questionId) {
                    it.answers.add(answer)
                }
            }
        }
    }

    fun addAnswer(questionId: String?, answer: List<Answer>) {
        experienceAnswer.value[_currentPageState.value]?.questionAnswers?.forEach {
            if (questionId == it.questionId) {
                it.answers = answer as ArrayList<Answer>
            }
        }
    }

    fun createRequest(questionnaireId: String, data: Topic?, position: Int) {
        var experience: ExperienceRequest? = null
        if (experienceAnswer.value.size > position) {
            experience = experienceAnswer.value[position]
        }
        val questionAnswers = arrayListOf<QuestionAnswer>()
        data?.questions?.forEach { question ->
            questionAnswers.add(
                QuestionAnswer(
                    questionId = question?.id,
                    answers = arrayListOf()
                )
            )
        }
        val newExperience = ExperienceRequest(
            questionnaireId = questionnaireId,
            topicId = data?.id,
            questionAnswers = questionAnswers
        )
        if (experience == null) {
            experienceAnswer.value.add(newExperience)
        } else {
            experienceAnswer.value[position] = newExperience
        }
        _currentPageState.value = position
    }

    fun submit() {
        viewModelScope.launch {
            experienceAnswer.value[_currentPageState.value]?.let { request ->
                updateQuestionnaireUseCase(request).collect {

                }
            }
        }
    }

    fun addFinancialIncome(source: String) {
        viewModelScope.launch {
            val sourceIncomes = _financialSourceIncomeState.value
            var newList: ArrayList<String> = arrayListOf()
            if (sourceIncomes.contains(source)) {
                newList =
                    sourceIncomes.filter { it.lowercase() != source.lowercase() } as ArrayList<String>
            } else {
                newList.addAll(sourceIncomes)
                newList.add(source)
            }
            _financialSourceIncomeState.value = newList
        }
    }

    fun addOtherAnswer(questionId: String?, answerId: String?, text: String?) {
        Timber.e("$questionId, $answerId $text ${experienceAnswer.value[_currentPageState.value]?.questionAnswers}")
        experienceAnswer.value[_currentPageState.value]?.questionAnswers?.forEach { questionAnswer ->
            if (questionId == questionAnswer.questionId) {
                questionAnswer.answers.mapIndexed { index, answer ->
                    if (answer.id == answerId) {
                        questionAnswer.answers[index].answer = text
                    }
                }
            }
        }
    }


}