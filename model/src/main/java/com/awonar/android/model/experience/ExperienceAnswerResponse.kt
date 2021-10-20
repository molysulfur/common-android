package com.awonar.android.model.experience

import com.google.gson.annotations.SerializedName

data class ExperienceAnswerResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("topics") val topic: List<TopicAnswer?>,
)

data class TopicAnswer(
    @SerializedName("id") val id: String?,
    @SerializedName("questions") val questions: List<QuestionAnswer?>?,
)