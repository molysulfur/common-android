package com.awonar.app.ui.setting.experience

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.experience.QuestionOption
import com.awonar.android.model.experience.Topic
import com.awonar.android.shared.domain.experience.GetExperienceQuestionUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.setting.experience.adapter.ExperienceItem
import com.molysulfur.library.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getExperienceQuestionUseCase: GetExperienceQuestionUseCase
) : ViewModel() {

    val experienceQuestion = getExperienceQuestionUseCase(Unit).map {
        it.data
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _experienceItem = MutableStateFlow<List<ExperienceItem?>>(emptyList())
    val experienceItem: StateFlow<List<ExperienceItem?>> get() = _experienceItem


    fun convertExperienceItem(title: String, subTitle: String, topic: Topic) {
        val itemList = arrayListOf<ExperienceItem>()
        itemList.add(ExperienceItem.Title(title))
        itemList.add(ExperienceItem.SubTitle(subTitle))
        topic.questions?.forEach { question ->
            when (question?.type) {
                QuestionType.CHOICE -> {
                    if (question.multiple) {
                        itemList.add(
                            ExperienceItem.CheckBoxQuestion(
                                title = question.title ?: "",
                                subTitle = question.description ?: "",
                                option = question.questionOption ?: arrayListOf()
                            )
                        )
                    } else {
                        itemList.add(
                            ExperienceItem.RadioQuestion(
                                title = question.title ?: "",
                                subTitle = question.description ?: "",
                                option = question.questionOption ?: arrayListOf()
                            )
                        )
                    }
                }
            }

        }
        _experienceItem.value = itemList
    }

}