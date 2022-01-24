package com.awonar.app.ui.socialtrade.filter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterAdapter
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import timber.log.Timber

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
                        "period", "status", "allocation" -> viewModel.navigate(
                            SocialTradeFilterFragmentDirections.socialTradeFilterFragmentToSocialTradeFilterPeriodFragment(
                                key))
                        "return", "risk", "numberCopy", "active", "profitable", "numberTrades", "daily", "weekly" -> viewModel.navigate(
                            SocialTradeFilterFragmentDirections.socialTradeFilterFragmentToSocialTradeFilterInputFragment(
                                key)
                        )
                    }
                }
                onChecked = {
                    viewModel.toggleMultiple(it)
                }
                onSingleChecked = {
                    viewModel.toggleSingle(it)
                }
                onCustomChange = { first, second ->
                    viewModel.updateCustomValue(first, second)
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