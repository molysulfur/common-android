package com.awonar.app.ui.history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.history.History
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.databinding.AwonarItemInstrumentOrderBinding

class HistoryViewHolder constructor(private val binding: AwonarItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(history: History) {
        binding.history = history
    }
}