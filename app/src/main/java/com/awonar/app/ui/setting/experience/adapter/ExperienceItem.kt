package com.awonar.app.ui.setting.experience.adapter

import android.os.Parcelable
import com.awonar.android.model.experience.QuestionOption
import kotlinx.parcelize.Parcelize

sealed class ExperienceItem(val type: Int) : Parcelable {

    @Parcelize
    data class Title(val title: String) : ExperienceItem(ExperienceItemType.EXPERIENCE_TITLE)

    @Parcelize
    data class SubTitle(val subTitle: String) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_SUBTITLE)

    @Parcelize
    data class RadioQuestion(
        val title: String,
        val subTitle: String,
        val option: List<QuestionOption?>
    ) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_RADIO)

    @Parcelize
    data class CheckBoxQuestion(val title: String, val subTitle: String, val option: List<QuestionOption?>) :
        ExperienceItem(ExperienceItemType.EXPERIENCE_CHECKBOX)

}