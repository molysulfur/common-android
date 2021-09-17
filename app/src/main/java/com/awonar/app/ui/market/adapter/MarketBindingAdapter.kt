package com.awonar.app.ui.market.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.awonar.android.model.market.Instrument
import com.awonar.app.ui.market.MarketPagerViewAdapter
import com.awonar.app.ui.market.MarketViewModel
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@BindingAdapter("setMarketAdapter", "marketViewModel")
fun setRecommended(
    recyclerView: RecyclerView,
    instrument: List<Instrument>,
    viewModel: MarketViewModel?
) {
    if (recyclerView.adapter == null) {
        val horizontalAdapter = InstrumentHorizontalAdapter(viewModel)
        val instrumentAdapter = InstrumentListAdapter(viewModel)
        val adapter = ConcatAdapter(
            InstrumentHorizontalWrapperAdapter(horizontalAdapter),
            instrumentAdapter
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.adapter = adapter

    }
    ((recyclerView.adapter as ConcatAdapter).adapters[0] as InstrumentHorizontalWrapperAdapter).adapter.itemList =
        instrument.filter { it.recommend }
    viewModel?.convertInstrumentToItem()
}

@BindingAdapter("categoryItem","viewModel")
fun setInstrumentCategoryItem(
    recyclerView: RecyclerView,
    instrument: List<InstrumentItem>,
    viewModel: MarketViewModel?
) {
    if (recyclerView.adapter == null) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = InstrumentListAdapter(viewModel)
    }
    (recyclerView.adapter as InstrumentListAdapter).itemList = instrument
}

@BindingAdapter("instrumentListItem","viewModel")
fun setInstrumentListITem(recyclerView: RecyclerView, instrument: List<InstrumentItem>,viewModel: MarketViewModel?) {
    if (recyclerView.adapter == null) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = InstrumentListAdapter(viewModel)
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