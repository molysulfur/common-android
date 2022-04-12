package com.awonar.android.model.feed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FeedPaging(
    var feeds: List<Feed?>,
    val page: Int,
) : Parcelable