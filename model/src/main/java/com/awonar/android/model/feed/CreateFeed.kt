package com.awonar.android.model.feed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CreateFeed(
    val description: String = "",
    val images: List<File> = listOf(),
    val sharePostId: String = ""
) : Parcelable