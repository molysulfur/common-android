package com.awonar.app.ui.feed.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemFeedLoadingBinding
import com.awonar.app.databinding.AwonarItemLoadingBinding
import kotlinx.coroutines.delay

class LoadingViewHolder constructor(private val binding: AwonarItemFeedLoadingBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(onLoad: (() -> Unit)?) {
        onLoad?.invoke()
    }
}