package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UserResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("about") val about: String? = null,
    @SerializedName("accountVerify") val accountVerify: String? = null,
    @SerializedName("address") val address: Address? = null,
    @SerializedName("appleId") val appleId: String? = null,
    @SerializedName("bankVerify") val bankVerify: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("birthDate") val birthDate: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("createdBy") val createdBy: String? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("displayFullName") val isDisplayFullName: Boolean = false,
    @SerializedName("email") val email: String? = null,
    @SerializedName("emailVerified") val isEmailVerified: Boolean = false,
    @SerializedName("facebook") val facebook: String? = null,
    @SerializedName("facebookId") val facebookId: String? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("middleName") val middleName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("followers") val followers: @RawValue List<Any> = emptyList(),
    @SerializedName("followings") val followings: @RawValue List<Any> = emptyList(),
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("googleId") val googleId: String? = null,
    @SerializedName("investmentSkills") val investmentSkills: String? = null,
    @SerializedName("inviteCode") val inviteCode: String? = null,
    @SerializedName("isProfessional") val isProfessional: Boolean = false,
    @SerializedName("license") val license: License? = null,
    @SerializedName("licenseId") val licenseId: String? = null,
    @SerializedName("linkedin") val linkedin: String? = null,
    @SerializedName("multiFactorAuthentication") val isMultiFactorAuthentication: Boolean = false,
    @SerializedName("otpChannel") val otpChannel: String? = null,
    @SerializedName("partnerExplain") val partnerExplain: String? = null,
    @SerializedName("partnerLv") val partnerLv: String? = null,
    @SerializedName("partnerRegisterAt") val partnerRegisterAt: String? = null,
    @SerializedName("partnerWebsite") val partnerWebsite: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("phoneVerified") val isPhoneVerified: Boolean = false,
    @SerializedName("picture") val picture: String? = null,
    @SerializedName("private") val isPrivate: Boolean = false,
    @SerializedName("referenceCode") val referenceCode: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("totalFollower") val totalFollower: Int = 0,
    @SerializedName("totalFollowing") val totalFollowing: Int = 0,
    @SerializedName("totalTrade") val totalTrade: Int = 0,
    @SerializedName("twitter") val twitter: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("updatedBy") val updatedBy: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("website") val website: String? = null,
    @SerializedName("youtube") val youtube: String? = null,
) : Parcelable {
    fun toUser(): User = User(
        id = this.id,
        avatar = this.thumbnail,
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        bio = this.bio,
        about = this.about,
        followerCount = this.totalFollower,
        followingCount = this.totalFollowing,
        copiesCount = this.totalTrade,
        isMe = true,
        bankVerify = this.bankVerify,
        accountVerifyType = User.getAccountVerifyType(this.accountVerify),
        skill = this.investmentSkills,
        facebookLink = this.facebook,
        twitterLink = this.twitter,
        linkedInLink = this.linkedin,
        youtubeLink = this.youtube,
        websiteLink = this.website,
        isPrivate = this.isPrivate,
        isDisplayFullName = this.isDisplayFullName,
        address = "%s, %s".format(this.address?.city ?: "", this.address?.countryId ?: ""),
        createdAt = this.createdAt
    )
}

@Parcelize
data class Address(
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("countryId") val countryId: String?,
    @SerializedName("postalCode") val postalCode: String?,
) : Parcelable {


}

@Parcelize
data class License(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("updatedBy") val updatedBy: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("createdBy") val createdBy: String?,
) : Parcelable
