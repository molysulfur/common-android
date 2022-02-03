package com.awonar.app.ui.profile.stat.holder

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemSelectorBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class SelectorViewHolder constructor(private val binding: AwonarItemSelectorBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(selectorItem: StatisticItem.SelectorItem) {
        with(binding.awonarItemSelectorInputSelector) {
            val adapter =
                ArrayAdapter(context, R.layout.awonar_item_text_view, selectorItem.selectorList)
            (editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }
}