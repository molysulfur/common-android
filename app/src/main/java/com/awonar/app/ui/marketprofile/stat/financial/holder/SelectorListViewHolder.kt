package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.graphics.RectF
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemSelectorListBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.widget.RectangleView
import com.github.mikephil.charting.data.BarEntry

class SelectorListViewHolder constructor(private val binding: AwonarItemSelectorListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: FinancialMarketItem.ListSelectorItem,
        position: Int,
        onSelect: ((FinancialMarketItem.BarEntryItem) -> Unit)?,
    ) {
        val colors =
            binding.root.context.resources.getStringArray(R.array.awonar_colors).toMutableList()
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
        with(binding.awonarSelectorListViewSelect) {
            removeAllViews()
            if (item.color >= 0) {
                val view = RectangleView(binding.root.context, colors[item.color])
                addView(view)
            }
        }

    }
}