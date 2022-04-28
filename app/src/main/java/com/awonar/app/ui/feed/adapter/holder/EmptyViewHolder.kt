package com.awonar.app.ui.feed.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemEmptyBinding

class EmptyViewHolder constructor(binding: AwonarItemEmptyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        val context = binding.root.context
        binding.title = context.getString(R.string.awonar_feed_title_empty)
        binding.title = context.getString(R.string.awoanr_feed_text_empty_description)
    }
}