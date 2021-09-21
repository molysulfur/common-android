package com.awonar.app.ui.setting

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.setting.adapter.SettingAdapter
import com.awonar.app.ui.setting.adapter.SettingItem


@BindingAdapter("settingItem")
fun setSettingAdapter(recyclerView: RecyclerView, listItem: List<SettingItem>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = SettingAdapter()
    }
    (recyclerView.adapter as SettingAdapter).apply {

    }
}