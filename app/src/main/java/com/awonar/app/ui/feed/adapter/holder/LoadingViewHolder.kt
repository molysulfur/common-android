package com.awonar.app.ui.feed.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemLoadingBinding

class LoadingViewHolder constructor(binding: AwonarItemLoadingBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(onLoad: (() -> Unit)?) {
        onLoad?.invoke()
    }
}