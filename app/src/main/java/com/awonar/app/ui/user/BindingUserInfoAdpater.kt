package com.awonar.app.ui.user

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.user.User
import com.awonar.app.R
import com.awonar.app.ui.user.adapter.UserInfoAdapter
import com.awonar.app.ui.user.adapter.UserInfoItem
import com.awonar.app.ui.user.adapter.UserInfoLayoutManager
import com.awonar.app.utils.DateUtils


@BindingAdapter("setUserInfoAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    userInfo: User?,
) {
    if (recyclerView.adapter == null) {
        val adapter = UserInfoAdapter()
        recyclerView.apply {
            layoutManager = UserInfoLayoutManager(recyclerView.context, adapter, 5)
            this.adapter = adapter
        }
    }
    val items = mutableListOf<UserInfoItem>()
    items.add(UserInfoItem.TitleItem("Overview"))
    items.add(UserInfoItem.DividerItem())
    items.add(UserInfoItem.SubTitleItem("About me"))
    items.add(UserInfoItem.TextItem(userInfo?.about ?: ""))
    items.add(UserInfoItem.SubTitleItem("Social network"))
    if (userInfo?.facebookLink.isNullOrBlank()) {
        items.add(UserInfoItem.BlankItem())
    } else {
        items.add(UserInfoItem.SocialItem(
            iconRes = R.drawable.awonar_ic_button_facebook,
            link = userInfo?.facebookLink))
    }
    if (userInfo?.twitterLink.isNullOrBlank()) {
        items.add(UserInfoItem.BlankItem())
    } else {
        items.add(UserInfoItem.SocialItem(
            iconRes = R.drawable.awonar_ic_button_twitter,
            link = userInfo?.twitterLink))
    }

    if (userInfo?.linkedInLink.isNullOrBlank()) {
        items.add(UserInfoItem.BlankItem())
    } else {
        items.add(UserInfoItem.SocialItem(
            iconRes = R.drawable.awonar_ic_button_linkedin,
            link = userInfo?.linkedInLink))
    }
    if (userInfo?.youtubeLink.isNullOrBlank()) {
        items.add(UserInfoItem.BlankItem())
    } else {
        items.add(UserInfoItem.SocialItem(
            iconRes = R.drawable.awonar_ic_button_youtube,
            link = userInfo?.youtubeLink))
    }
    if (userInfo?.websiteLink.isNullOrBlank()) {
        items.add(UserInfoItem.BlankItem())
    } else {
        items.add(UserInfoItem.SocialItem(
            iconRes = R.drawable.awonar_ic_button_linkedin,
            link = userInfo?.websiteLink))
    }
    items.add(UserInfoItem.SubTitleItem("Country"))
    items.add(UserInfoItem.TextItem(userInfo?.address ?: ""))
    items.add(UserInfoItem.SubTitleItem("Join on"))
    items.add(UserInfoItem.TextItem(DateUtils.getDate(userInfo?.createdAt ?: "")))
    items.add(UserInfoItem.SubTitleItem("Investment Skill"))
    items.add(UserInfoItem.TextItem(userInfo?.skill ?: ""))
    (recyclerView.adapter as UserInfoAdapter).apply {
        itemList = items
    }
}