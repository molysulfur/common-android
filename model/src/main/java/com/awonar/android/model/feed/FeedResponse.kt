package com.awonar.android.model.feed

import com.awonar.android.model.core.Meta
import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("posts") val feeds: List<Feed?>?,
    @SerializedName("meta") val meta: Meta?,
)