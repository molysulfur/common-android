package com.awonar.app.ui.profile.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.awonar.app.databinding.AwonarItemLoadingBinding
import com.awonar.app.databinding.AwonarItemPositionBinding
import com.awonar.app.ui.profile.history.adapter.holder.LoadMoreViewHolder
import com.awonar.app.ui.profile.history.adapter.holder.PositionViewHolder

class HistoryProfileAdapter : RecyclerView.Adapter<ViewHolder>() {

    var itemLists: MutableList<HistoryProfileItem> = mutableListOf()
        set(value) {
            val old = field
            field = value

        }

    var columns: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((Int) -> Unit)? = null
    var onLoad: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is PositionViewHolder -> holder.bind(item as HistoryProfileItem.PositionItem,position,onClick)
            is LoadMoreViewHolder -> {
                onLoad?.invoke((item as HistoryProfileItem.LoadMoreItem).page)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            HistoryProfileType.POSITION_HISTORY_PROFILE -> PositionViewHolder(
                AwonarItemPositionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HistoryProfileType.LOADMORE -> LoadMoreViewHolder(
                AwonarItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("View type is not found : $viewType")
        }

    override fun getItemCount(): Int = itemLists.size

}