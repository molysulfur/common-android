package com.awonar.android.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var avatar: String? = null,
    var username: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var middleName: String? = null,
    var bio: String? = null,
    var about: String? = null,
    var followerCount: Int = 0,
    var followingCount: Int = 0,
    var copiesCount: Int = 0,
    var isMe: Boolean = false,
    var accountVerifyType: AccountVerifyType? = null,
    var bankVerify: String? = null,
    var skill: String? = null,
    var facebookLink: String? = null,
    var twitterLink: String? = null,
    var linkedInLink: String? = null,
    var youtubeLink: String? = null,
    var websiteLink: String? = null,
    var isDisplayFullName: Boolean = false,
    var isPrivate: Boolean = false,
    var address: String? = null,
    var createdAt: String? = null
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