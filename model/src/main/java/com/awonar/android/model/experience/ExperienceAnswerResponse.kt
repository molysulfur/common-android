package com.awonar.android.model.experience

import com.google.gson.annotations.SerializedName

data class ExperienceAnswerResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("topic") val topic: String?,
)

data class TopicAnswer(
    @SerializedName("id") val id: String?,
    @SerializedName("questions") val questions: List<QuestionAnswer?>?,
)