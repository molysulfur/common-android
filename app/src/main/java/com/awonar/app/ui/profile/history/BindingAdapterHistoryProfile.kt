package com.awonar.app.ui.profile.history

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.profile.history.adapter.HistoryProfileAdapter
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem

@BindingAdapter("setHistoryPortfolio", "viewModel", "column1", "column2", "column3")
fun setHistoryPortfolio(
    recycler: RecyclerView,
    itemList: MutableList<HistoryProfileItem>,
    viewModel: HistoryProfileViewModel?,
    column1: String?,
    column2: String?,
    column3: String?,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            adapter = HistoryProfileAdapter().apply {
                onLoad = {
                    viewModel?.getHistoryPosition()
                }
                onClick = { index ->
                    viewModel?.openInside(index)
                }
            }
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    with(recycler.adapter as HistoryProfileAdapter) {
        itemLists = itemList
        columns = arrayListOf(column1 ?: "", column2 ?: "", column3 ?: "", "")
    }
}
