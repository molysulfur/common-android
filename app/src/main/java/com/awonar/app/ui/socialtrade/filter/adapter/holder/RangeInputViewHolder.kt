package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInputRangeBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

class RangeInputViewHolder constructor(private val binding: AwonarItemInputRangeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: SocialTradeFilterItem.RangeInputItem,
        onClick: ((String?, String?) -> Unit)?,
    ) {
        binding.awonarInputRangeInputFirst.editText?.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                onClick?.invoke(
                    it.toString(),
                    binding.awonarInputRangeInputSecond.editText?.toString()
                )
            }
        }
        binding.awonarInputRangeInputSecond.editText?.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                onClick?.invoke(
                    binding.awonarInputRangeInputFirst.editText?.toString(),
                    it.toString(),
                )
            }
        }
    }
}