package com.awonar.android.model.experience

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("topics") val topics: List<Topic?>?,
    @SerializedName("active") val active: Boolean
) : Parcelable

@Parcelize
data class Topic(
    @SerializedName("id") val id: String?,
    @SerializedName("questionnaireId") val topicId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("questions") val questions: List<Question?>?
) : Parcelable

@Parcelize
data class Question(
    @SerializedName("id") val id: String?,
    @SerializedName("topicId") val topicId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: Int,
    @SerializedName("multiple") val multiple: Boolean,
    @SerializedName("required") val required: Boolean,
    @SerializedName("questionOption") val questionOption: List<QuestionOption?>?,
    @SerializedName("compatibleOptions") val compatibleOptions: List<String?>?
) : Parcelable

@Parcelize
data class QuestionOption(
    @SerializedName("id") val id: String?,
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("compatibleOptions") val compatibleOptions: List<String?>?,
    @SerializedName("answer") val answer: String?,
    @SerializedName("image") val image: String?
) : Parcelable


