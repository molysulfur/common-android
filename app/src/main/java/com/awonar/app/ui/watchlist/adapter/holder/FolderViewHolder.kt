package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.watchlist.Folder
import com.awonar.app.databinding.AwonarItemFolderWatchlistBinding

class FolderViewHolder constructor(private val binding: AwonarItemFolderWatchlistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val LIMIT_RECENT = 8
    }

    fun bind(
        watchlistFolder: Folder,
        onCardClick: ((String) -> Unit)?,
        onClose: ((String) -> Unit)?,
    ) {
        with(binding.awonarFolderWatchlistItem) {
            setOnClickListener {
                onCardClick?.invoke(watchlistFolder.id)
            }
            this.onClose = {
                onClose?.invoke(watchlistFolder.id)
            }
            setTitle(watchlistFolder.name ?: "")
            setSubTitle("items : ${watchlistFolder.totalItem}")
            isCloseIconVisible(!watchlistFolder.static)
            setRecentImage(watchlistFolder.images.filterIndexed { index, image -> !image.isNullOrEmpty() && index < LIMIT_RECENT })
        }
    }
}