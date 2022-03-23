package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemDropdownBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import timber.log.Timber

class SelectorDropdownViewHolder constructor(private val binding: AwonarItemDropdownBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: FinancialMarketItem.DropdownItem, onDateSelect: ((String) -> Unit)?) {
        val adapter = ArrayAdapter(binding.root.context, R.layout.awonar_item_text, item.selectors)
        with(binding.awonarDropdownInputItem) {
            (editText as? AutoCompleteTextView)?.apply {
                setText(item.selectors[0])
                setAdapter(adapter)
                doAfterTextChanged {
                    val text = it.toString()
                    if (item.default != text) {
                        onDateSelect?.invoke(text)
                    }
                }
            }
        }
        binding.hint = item.text
    }
}