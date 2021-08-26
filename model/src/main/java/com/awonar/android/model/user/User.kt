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
    val bio: String?,
    val about: String?,
    val followerCount: Int,
    val followingCount: Int,
    val copiesCount: Int,
    val isMe: Boolean,
    val accountVerifyType: AccountVerifyType?,
    val skill: String?,
    val facebookLink: String?,
    val twitterLink: String?,
    val linkedInLink: String?,
    val youtubeLink: String?,
    val websiteLink: String?,
) : Parcelable {

    companion object {
        fun getAccountVerifyType(stringType: String?): AccountVerifyType? = when (stringType) {
            "prepare" -> AccountVerifyType.PREPARE
            "pending" -> AccountVerifyType.PENDING
            "approve" -> AccountVerifyType.APPROVE
            "reject" -> AccountVerifyType.REJECT
            else -> null
        }
    }
}

enum class AccountVerifyType {
    PREPARE,
    PENDING,
    APPROVE,
    REJECT
}