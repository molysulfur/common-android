package com.awonar.app.ui.watchlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.watchlist.Folder
import com.awonar.app.databinding.AwonarItemFolderWatchlistBinding
import com.awonar.app.ui.watchlist.adapter.holder.FolderViewHolder

class WatchlistFolderAdapter : RecyclerView.Adapter<FolderViewHolder>() {

    var itemList: MutableList<Folder> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardClick: ((String) -> Unit)? = null
    var onClose: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder =
        FolderViewHolder(
            AwonarItemFolderWatchlistBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(itemList[position], onCardClick, onClose)
    }

    override fun getItemCount(): Int = itemList.size
}