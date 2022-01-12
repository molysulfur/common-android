package com.awonar.app.ui.user.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class UserInfoLayoutManager(context: Context, adapter: RecyclerView.Adapter<*>, spanCount: Int) :
    GridLayoutManager(context, spanCount) {
    init {
        spanSizeLookup = UserInfoLookup(adapter, spanCount)
    }

    override fun canScrollVertically(): Boolean = false
}
