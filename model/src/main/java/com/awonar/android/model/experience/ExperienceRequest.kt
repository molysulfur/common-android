package com.awonar.android.model.experience

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceRequest(
    @SerializedName("questionnaireId") var questionnaireId: String?,
    @SerializedName("topicId") var topicId: String?,
    @SerializedName("questions") var questionAnswers: ArrayList<QuestionAnswer>
) : Parcelable

@Parcelize
data class QuestionAnswer(
    @SerializedName("id") var questionId: String?,
    @SerializedName("options") var answers: ArrayList<Answer>
) : Parcelable

@Parcelize
data class Answer(
    @SerializedName("id") var id: String?,
    @SerializedName("answer") var answer: String? = null
) : Parcelable