package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemViewpagerBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class MenuTabsViewHolder constructor(private val binding: AwonarItemViewpagerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FinancialMarketItem.TabsItem, onSelected: ((String?) -> Unit)?) {
        with(binding.awonarItemViewpagerTabs) {
            removeAllTabs()
            item.tabs.forEach {
                val tab = binding.awonarItemViewpagerTabs.newTab()
                tab.text = it
                tab.contentDescription = it
                addTab(tab)
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Timber.e("${tab?.text}, ${item.current}")
                    if (tab?.text != item.current && item.current != null) {
                        onSelected?.invoke(tab?.text.toString())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }
}