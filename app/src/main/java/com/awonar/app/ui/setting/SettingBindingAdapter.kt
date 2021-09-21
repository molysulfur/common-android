package com.awonar.app.ui.setting

import android.content.Intent
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.ui.setting.adapter.SettingAdapter
import com.awonar.app.ui.setting.adapter.SettingItem


@BindingAdapter("settingItem")
fun setSettingAdapter(recyclerView: RecyclerView, listItem: List<SettingItem>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = SettingAdapter()
        (recyclerView.adapter as SettingAdapter).apply {
            onClick = { activity ->
                recyclerView.context.startActivity(Intent(recyclerView.context, activity))
            }
        }
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
    }
    (recyclerView.adapter as SettingAdapter).apply {
        itemLists = listItem ?: emptyList()
    }
}

@BindingAdapter("setAlertIcon")
fun setIconAlert(button: Button, isAlert: Boolean) {
    if (isAlert) {
        button.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.awonar_ic_yellow_warning,
            0
        )
    } else {
        button.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            0,
            0
        )
    }
}