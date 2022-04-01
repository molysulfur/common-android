package com.awonar.app.ui.search

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.ui.marketprofile.MarketProfileActivity
import com.awonar.app.ui.profile.ProfileActivity
import com.awonar.app.ui.search.adapter.SearchAdapter
import com.awonar.app.ui.search.adapter.SearchItem

@BindingAdapter("searchResult", "viewModel")
fun setSearchAdapter(
    recyclerView: RecyclerView,
    itemLists: MutableList<SearchItem>,
    viewModel: SearchViewModel,
) {
    if (recyclerView.adapter == null) {
        with(recyclerView) {
            adapter = SearchAdapter().apply {
                onClear = {
                    viewModel.clearRecently()
                }
                onItemClick = { id, isMarket ->
                    if (isMarket) {
                        val newIntent =
                            Intent(recyclerView.context, MarketProfileActivity::class.java)
                        newIntent.putExtra(MarketProfileActivity.INSTRUMENT_EXTRA, id?.toInt())
                        recyclerView.context.startActivity(newIntent)
                    } else {
                        val newIntent =
                            Intent(recyclerView.context, ProfileActivity::class.java)
                        newIntent.putExtra(ProfileActivity.EXTRA_USERID, id)
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