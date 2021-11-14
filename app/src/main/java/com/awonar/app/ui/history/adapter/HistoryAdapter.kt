package com.awonar.app.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.awonar.android.model.history.History
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding

class HistoryAdapter(
    private val diff: DiffUtil.ItemCallback<History> = object :
        DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(
            oldItem: History,
            newItem: History
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: History,
            newItem: History
        ): Boolean {
            return oldItem == newItem
        }
    }
) : PagingDataAdapter<History, HistoryViewHolder>(diff) {

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        item?.let{
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(
            AwonarItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}