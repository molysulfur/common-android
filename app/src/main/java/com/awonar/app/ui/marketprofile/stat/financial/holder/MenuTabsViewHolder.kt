package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemViewpagerBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class MenuTabsViewHolder constructor(private val binding: AwonarItemViewpagerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FinancialMarketItem.TabsItem, onSelected: ((String?) -> Unit)?) {
        with(binding.awonarItemViewpagerTabs) {
            check(when (item.current) {
                "Statistic" -> R.id.awonar_item_chip_stat
                "Income Statement" -> R.id.awonar_item_chip_income
                "Balance Sheet" -> R.id.awonar_item_chip_balance
                "Cashflow" -> R.id.awonar_item_chip_cashflow
                else -> R.id.awonar_item_chip_stat
            })
            setOnCheckedChangeListener { group, checkedId ->
                val checkText = when (checkedId) {
                    R.id.awonar_item_chip_stat -> "Statistic"
                    R.id.awonar_item_chip_income -> "Income Statement"
                    R.id.awonar_item_chip_balance -> "Balance Sheet"
                    R.id.awonar_item_chip_cashflow -> "Cashflow"
                    else -> null
                }
                onSelected?.invoke(checkText)
            }
        }
    }
}