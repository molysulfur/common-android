package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInputRangeBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import timber.log.Timber

class RangeInputViewHolder constructor(private val binding: AwonarItemInputRangeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: SocialTradeFilterItem.RangeInputItem,
        onChange: ((String?, String?) -> Unit)?,
    ) {
        with(binding.awonarInputRangeInputFirst) {
            hint = item.placeholder1
            editText?.doAfterTextChanged {
                if (!it.isNullOrEmpty()) {
                    onChange?.invoke(
                        it.toString(),
                        binding.awonarInputRangeInputSecond.editText?.text.toString()
                    )
                }
            }
        }

        with(binding.awonarInputRangeInputSecond) {
            hint = item.placeholder2
            editText?.doAfterTextChanged {
                if (!it.isNullOrEmpty()) {
                    onChange?.invoke(
                        binding.awonarInputRangeInputFirst.editText?.text.toString(),
                        it.toString(),
                    )
                }
            }
        }

    }
}