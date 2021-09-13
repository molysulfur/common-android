package com.awonar.app.ui.setting.experience.adapter

import android.os.Parcelable
import com.awonar.android.model.experience.QuestionOption
import kotlinx.parcelize.Parcelize

sealed class ExperienceItem(val type: Int) : Parcelable {

    @Parcelize
    class Blank : ExperienceItem(ExperienceItemType.EXPERIENCE_BLANK)

    @Parcelize
    data class Title(val title: String) : ExperienceItem(ExperienceItemType.EXPERIENCE_TITLE)

    @Parcelize
    data class SubTitle(val subTitle: String) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_SUBTITLE)

    @Parcelize
    data class RadioQuestion(
        val questionId: String,
        val topicId: String,
        val option: List<QuestionOption?>?,
        val enable: Boolean = true
    ) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_RADIO)

    @Parcelize
    data class CheckBoxQuestion(
        val questionId: String,
        val topicId: String,
        val option: List<QuestionOption?>?,
        var enable: Boolean = true
    ) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_CHECKBOX)


}