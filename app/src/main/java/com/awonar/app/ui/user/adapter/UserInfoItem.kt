package com.awonar.app.ui.user.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class UserInfoItem(
    val type: Int,
) : Parcelable {

    @Parcelize
    data class TitleItem(
        val title: String,
    ) : UserInfoItem(UserInfoType.TITLE_USER_INFO)


    @Parcelize
    data class SubTitleItem(
        val subTitle: String,
    ) : UserInfoItem(UserInfoType.SUBTITLE_USER_INFO)


    @Parcelize
    data class TextItem(
        val text: String,
    ) : UserInfoItem(UserInfoType.TEXT_USER_INFO)

    @Parcelize
    data class SocialItem(
        val iconRes: Int = 0,
        val icon: String? = null,
        val link: String?,
    ) : UserInfoItem(UserInfoType.SOCIAL_USER_INFO)

    @Parcelize
    class BlankItem: UserInfoItem(UserInfoType.BLANK_USER_INFO)
    @Parcelize
    class DividerItem: UserInfoItem(UserInfoType.DIVIDER_USER_INFO)

}