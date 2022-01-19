package com.awonar.app.ui.socialtrade.filter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterAdapter
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import timber.log.Timber

@BindingAdapter("socialTradeFilterAdapter")
fun setSocialTradeFilter(
    recycler: RecyclerView,
    newItemList: MutableList<SocialTradeFilterItem>,
) {
    Timber.e("$newItemList")
    if (recycler.adapter == null) {
        val newAdapter = SocialTradeFilterAdapter()
        with(recycler) {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = newAdapter
        }
    }
    with(recycler.adapter as SocialTradeFilterAdapter) {
        this.itemList = newItemList
    }

}