package com.awonar.app.ui.watchlist

import android.graphics.Canvas
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.watchlist.Folder
import com.awonar.app.ui.watchlist.adapter.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import timber.log.Timber

@BindingAdapter("setWatchlistAdapter", "viewModel")
fun setWatchlistAdapter(
    recycler: RecyclerView,
    itemList: MutableList<WatchlistItem>,
    viewModel: WatchlistViewModel,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            setTouchHelper(recycler, viewModel)
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = WatchlistAdapter()
        }
    }
    with(recycler.adapter as WatchlistAdapter) {
        this.itemList = itemList
    }
}

private fun setTouchHelper(recycler: RecyclerView, viewModel: WatchlistViewModel) {
    val touchHelperCallback = WatchlistTouchHelperCallback(
        object : IWatchlistTouchHelperCallback {
            override fun onClose(position: Int) {
                viewModel.removeItem(position)
            }
        }, recycler.context
    )
    val helper = ItemTouchHelper(touchHelperCallback)
    helper.attachToRecyclerView(recycler)
    recycler.addItemDecoration(object :
        RecyclerView.ItemDecoration() {

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            touchHelperCallback.onDraw(c)
        }
    })
}

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
                onCardClick = {
                    viewModel.navigate(it)
                }
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