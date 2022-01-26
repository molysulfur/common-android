package com.awonar.app.ui.watchlist

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.watchlist.Folder
import com.awonar.app.ui.watchlist.adapter.WatchlistFolderAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import timber.log.Timber

@BindingAdapter("setFolderAdapter", "viewModel")
fun setFolderAdapter(
    recycler: RecyclerView,
    itemList: List<Folder>,
    viewModel: WatchlistViewModel,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = WatchlistFolderAdapter().apply {
                onClose = {
                    viewModel.deleteFolder(it)
                }
            }
        }
    }
    with(recycler.adapter as WatchlistFolderAdapter) {
        this.itemList = itemList.toMutableList()
    }
}

@BindingAdapter("visibleProgress")
fun setProgressVisible(
    progress: CircularProgressIndicator,
    isVisible: Boolean,
) {
    progress.visibility = if (isVisible) View.VISIBLE else View.GONE
}