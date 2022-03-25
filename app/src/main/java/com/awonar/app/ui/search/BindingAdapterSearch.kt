package com.awonar.app.ui.search

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.marketprofile.MarketProfileActivity
import com.awonar.app.ui.search.adapter.SearchAdapter
import com.awonar.app.ui.search.adapter.SearchItem

@BindingAdapter("searchResult")
fun setSearchAdapter(
    recyclerView: RecyclerView,
    itemLists: MutableList<SearchItem>,
) {
    if (recyclerView.adapter == null) {
        with(recyclerView) {
            adapter = SearchAdapter().apply {
                onItemClick = { id, isMarket ->
                    if (isMarket) {
                        val newIntent =
                            Intent(recyclerView.context, MarketProfileActivity::class.java)
                        newIntent.putExtra(MarketProfileActivity.INSTRUMENT_EXTRA, id?.toInt())
                        recyclerView.context.startActivity(newIntent)
                    }
                }
            }
            layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }
    }
    (recyclerView.adapter as SearchAdapter).itemList = itemLists
}