package com.awonar.app.ui.watchlist

import android.graphics.Canvas
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.watchlist.Folder
import com.awonar.app.ui.watchlist.adapter.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("setWatchlistAdapter", "viewModel", "enableTouchHelper")
fun setWatchlistAdapter(
    recycler: RecyclerView,
    itemList: MutableList<WatchlistItem>,
    viewModel: WatchlistViewModel,
    isEnable: Boolean? = false,
) {
    if (recycler.adapter == null) {
        with(recycler) {
            if (isEnable == true) {
                setTouchHelper(recycler, viewModel)
            }
            layoutManager =
                LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
            adapter = WatchlistAdapter().apply {
                onButtonClick = {
                    viewModel.openAddWatchlist()
                }
            }
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
                    viewModel.navigate(WatchlistFolderFragmentDirections.watchlistFolderFragmentToWatchlistListFragment(
                        it))
                }
                onClose = {
                    MaterialAlertDialogBuilder(recycler.context)
                        .setTitle("Are you sure?")
                        .setMessage("You want to delete folder?")
                        .setPositiveButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setNegativeButton("Delete") { dialog, which ->
                            viewModel.deleteFolder(it)
                        }
                        .show()
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