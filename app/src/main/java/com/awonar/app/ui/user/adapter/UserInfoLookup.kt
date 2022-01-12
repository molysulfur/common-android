package com.awonar.app.ui.user.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserInfoLookup(private val adapter: RecyclerView.Adapter<*>, private val columnCount: Int) :
    GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int = when (adapter.getItemViewType(position)) {
        UserInfoType.TITLE_USER_INFO -> columnCount
        UserInfoType.SUBTITLE_USER_INFO -> columnCount
        UserInfoType.TEXT_USER_INFO -> columnCount
        UserInfoType.SOCIAL_USER_INFO -> 1
        else -> 1
    }
}
