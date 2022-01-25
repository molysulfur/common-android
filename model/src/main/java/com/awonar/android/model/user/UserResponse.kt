package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UserResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("about") val about: String?,
    @SerializedName("accountVerify") val accountVerify: String?,
    @SerializedName("address") val address: Address?,
    @SerializedName("appleId") val appleId: String?,
    @SerializedName("bankVerify") val bankVerify: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("createdBy") val createdBy: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("displayFullName") val isDisplayFullName: Boolean,
    @SerializedName("email") val email: String?,
    @SerializedName("emailVerified") val isEmailVerified: Boolean,
    @SerializedName("facebook") val facebook: String?,
    @SerializedName("facebookId") val facebookId: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("followers") val followers: @RawValue List<Any>,
    @SerializedName("followings") val followings: @RawValue List<Any>,
    @SerializedName("gender") val gender: String?,
    @SerializedName("googleId") val googleId: String?,
    @SerializedName("investmentSkills") val investmentSkills: String?,
    @SerializedName("inviteCode") val inviteCode: String?,
    @SerializedName("isProfessional") val isProfessional: Boolean,
    @SerializedName("license") val license: License?,
    @SerializedName("licenseId") val licenseId: String?,
    @SerializedName("linkedin") val linkedin: String?,
    @SerializedName("multiFactorAuthentication") val isMultiFactorAuthentication: Boolean,
    @SerializedName("otpChannel") val otpChannel: String?,
    @SerializedName("partnerExplain") val partnerExplain: String?,
    @SerializedName("partnerLv") val partnerLv: String?,
    @SerializedName("partnerRegisterAt") val partnerRegisterAt: String?,
    @SerializedName("partnerWebsite") val partnerWebsite: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("phoneVerified") val isPhoneVerified: Boolean,
    @SerializedName("picture") val picture: String?,
    @SerializedName("private") val isPrivate: Boolean,
    @SerializedName("referenceCode") val referenceCode: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("totalFollower") val totalFollower: Int,
    @SerializedName("totalFollowing") val totalFollowing: Int,
    @SerializedName("totalTrade") val totalTrade: Int,
    @SerializedName("twitter") val twitter: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("updatedBy") val updatedBy: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("website") val website: String?,
    @SerializedName("youtube") val youtube: String?,
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
