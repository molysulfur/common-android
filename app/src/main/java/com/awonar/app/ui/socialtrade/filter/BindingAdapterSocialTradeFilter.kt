package com.awonar.app.ui.socialtrade.filter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterAdapter
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

@BindingAdapter("socialTradeFilterAdapter", "viewModel")
fun setSocialTradeFilter(
    recycler: RecyclerView,
    newItemList: MutableList<SocialTradeFilterItem>,
    viewModel: SocialTradeFilterViewModel,
) {
    val context = recycler.context
    if (recycler.adapter == null) {
        val newAdapter = SocialTradeFilterAdapter()
            .apply {
                onClick = { key ->
                    when (key) {
                        "period" -> viewModel.navigate(SocialTradeFilterFragmentDirections.socialTradeFilterFragmentToSocialTradeFilterSelectorFragment(key))
                    }
                }
                onChecked = {
                    viewModel.toggleTimePeriod(it)
                }
            }
        with(recycler) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = newAdapter
        }
    }
    with(recycler.adapter as SocialTradeFilterAdapter) {
        this.itemList = newItemList
    }

}