package com.awonar.app.ui.columns.activedadapter

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding

class ColumnItemListViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String, onClick: ((String) -> Unit)?) {
        with(binding.awonarItemListText) {
            setTitle(text)
            setOnClickListener {
                onClick?.invoke(text)
            }
        }

    }
}