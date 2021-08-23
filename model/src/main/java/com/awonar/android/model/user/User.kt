package com.awonar.android.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String?,
    val avatar: String?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val about: String?,
    val followerCount: Int,
    val followingCount: Int,
    val copiesCount: Int
) : Parcelable