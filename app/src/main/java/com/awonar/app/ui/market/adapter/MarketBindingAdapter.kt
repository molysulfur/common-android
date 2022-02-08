package com.awonar.app.ui.market.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.market.MarketViewModel
import io.supercharge.shimmerlayout.ShimmerLayout

@BindingAdapter("categoryItem", "viewModel")
fun setInstrumentCategoryItem(
    recyclerView: RecyclerView,
    instrument: List<InstrumentItem>,
    viewModel: MarketViewModel?
) {
    if (recyclerView.adapter == null) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = InstrumentListAdapter().apply {
            onViewMoreClick = { arg ->
                viewModel?.onViewMore(arg)
            }
        }
    }
    (recyclerView.adapter as InstrumentListAdapter).itemList = instrument
}

@BindingAdapter("instrumentListItem", "viewModel")
fun setInstrumentListITem(
    recyclerView: RecyclerView,
    instrument: List<InstrumentItem>,
    viewModel: MarketViewModel?
) {
    if (recyclerView.adapter == null) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = InstrumentListAdapter()
    }
    (recyclerView.adapter as InstrumentListAdapter).itemList = instrument
}

@BindingAdapter("recommenedItem")
fun setInstrumentItem(recyclerView: RecyclerView, instrument: List<InstrumentItem>) {
    if (recyclerView.adapter != null) {
        ((recyclerView.adapter as ConcatAdapter).adapters[1] as InstrumentListAdapter).itemList =
            instrument
    }
}

@BindingAdapter("startLoading")
fun setLoading(progress: ShimmerLayout, isStart: Boolean) {
    if (isStart)
        progress.startShimmerAnimation()
    else
        progress.stopShimmerAnimation()
}