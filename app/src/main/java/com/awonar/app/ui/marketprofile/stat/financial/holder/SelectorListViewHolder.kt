package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.icu.text.CompactDecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemSelectorListBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.widget.RectangleView
import com.github.mikephil.charting.data.BarEntry
import java.util.*


class SelectorListViewHolder constructor(private val binding: AwonarItemSelectorListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: FinancialMarketItem.ListSelectorItem,
        onSelect: ((String) -> Unit)?,
    ) {
        val colors =
            binding.root.context.resources.getStringArray(R.array.awonar_colors).toMutableList()
        with(binding.awonarSelectorListItem) {
            setTitle(item.text ?: "")
            val meta = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val compactCurrency =
                    CompactDecimalFormat.getInstance(
                        Locale.US,
                        CompactDecimalFormat.CompactStyle.SHORT)
                val currency = Currency.getInstance(Locale.US)
                val amount = CurrencyAmount(item.value, currency)
                compactCurrency.format(amount)
            } else {
                item.value

            }
            setMeta("%s".format(meta))
            setOnClickListener {
                onSelect?.invoke(item.key ?: "")
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