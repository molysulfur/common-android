package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.graphics.RectF
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSelectorListBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.widget.RectangleView
import com.github.mikephil.charting.data.BarEntry

class SelectorListViewHolder constructor(private val binding: AwonarItemSelectorListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: FinancialMarketItem.ListSelectorItem,
        onSelect: ((FinancialMarketItem.BarEntryItem) -> Unit)?,
    ) {
        with(binding.awonarSelectorListItem) {
            setTitle(item.text ?: "")
            setMeta("%s".format(item.value))
            setOnClickListener {
                onSelect?.invoke(FinancialMarketItem.BarEntryItem(
                    title = item.text ?: "",
                    entries = arrayListOf(BarEntry(0f, item.value.toFloat()))
                ))
            }
        }
        if(item.isSelected){
            val view = RectangleView(binding.root.context, "#FF0000")
            binding.awonarSelectorListViewSelect.apply {
                removeAllViews()
                addView(view)
            }
        }

    }
}