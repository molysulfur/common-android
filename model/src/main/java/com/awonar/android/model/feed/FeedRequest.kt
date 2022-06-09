package com.awonar.android.model.feed

data class FeedRequest(
    val page: Int,
    val type: String = "",
    val prefixPath: String = ""
)