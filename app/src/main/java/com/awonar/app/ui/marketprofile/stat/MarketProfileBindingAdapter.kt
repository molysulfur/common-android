package com.awonar.app.ui.marketprofile.stat

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketItem
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketAdapter

@BindingAdapter("overviewAdapter")
fun setOverViewAdapter(
    recyclerView: RecyclerView,
    marketItemLists: MutableList<OverviewMarketItem>,
) {
    if (recyclerView.adapter == null) {
        with(recyclerView) {
            adapter = OverviewMarketAdapter()
            layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    with(recyclerView.adapter as OverviewMarketAdapter) {
        itemList = marketItemLists
    }
}
