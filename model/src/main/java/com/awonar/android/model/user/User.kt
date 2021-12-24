package com.awonar.android.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String?,
    var avatar: String?,
    var username: String?,
    var firstName: String?,
    var lastName: String?,
    var middleName: String?,
    var bio: String?,
    var about: String?,
    var followerCount: Int,
    var followingCount: Int,
    var copiesCount: Int,
    var isMe: Boolean,
    var accountVerifyType: AccountVerifyType?,
    var bankVerify: String?,
    var skill: String?,
    var facebookLink: String?,
    var twitterLink: String?,
    var linkedInLink: String?,
    var youtubeLink: String?,
    var websiteLink: String?,
    var isDisplayFullName: Boolean,
    var isPrivate: Boolean,
    var address: String?,
    var createdAt : String?
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