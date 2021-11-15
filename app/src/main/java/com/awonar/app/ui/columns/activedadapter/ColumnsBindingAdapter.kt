package com.awonar.app.ui.columns.activedadapter

import android.app.Activity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.ui.columns.ColumnsViewModel
import com.google.android.material.appbar.MaterialToolbar

@BindingAdapter("setActivedColumnToolbar")
fun setActivedColumnToolbar(toolbar: MaterialToolbar, viewModel: ColumnsViewModel?) {
    toolbar.setNavigationOnClickListener {
        (toolbar.context as Activity).finish()
    }
    toolbar.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.awonar_toolbar_actived_column_save -> viewModel?.saveActivedColumn()
            R.id.awonar_toolbar_actived_column_reset -> viewModel?.resetActivedColumn()
        }
        (toolbar.context as Activity).run {
            setResult(Activity.RESULT_OK)
            finish()
        }
        false
    }
}

@BindingAdapter("setColumnsAdapter", "viewModel")
fun setActivedColumn(
    recycler: RecyclerView,
    activedList: List<String>,
    viewModel: ColumnsViewModel
) {
    if (recycler.adapter == null) {
        recycler.apply {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = ActivedColumnAdapter().apply {
                onClick = { text ->
                    viewModel.activedColumnChange(text)
                }
            }
        }
    }
    (recycler.adapter as ActivedColumnAdapter).itemList = activedList
}
