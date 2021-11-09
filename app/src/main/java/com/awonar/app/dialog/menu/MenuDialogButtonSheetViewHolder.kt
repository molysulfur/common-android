package com.awonar.app.dialog.menu

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonItemBinding
import com.awonar.app.databinding.AwonarItemListBinding

class MenuDialogButtonSheetViewHolder constructor(private val binding: AwonarItemButtonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MenuDialog, onClick: ((MenuDialog) -> Unit)?) {
        binding.text = item.text
        binding.awonarButtonItemButtonList.setOnClickListener {
            onClick?.invoke(item)
        }
    }
}