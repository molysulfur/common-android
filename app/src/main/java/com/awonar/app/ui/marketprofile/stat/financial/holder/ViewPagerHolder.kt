package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemViewpagerBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.ui.marketprofile.stat.financial.FinancialViewPagerAdapter
import com.awonar.app.ui.profile.adapter.ProfilePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerHolder constructor(private val binding: AwonarItemViewpagerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(marketItem: FinancialMarketItem.ViewPagerItem, fragmentActivity: FragmentActivity) {
        with(binding.awonarItemViewpagerPage) {
            adapter = FinancialViewPagerAdapter(fragmentActivity)
        }
        TabLayoutMediator(binding.awonarItemViewpagerTabs,
            binding.awonarItemViewpagerPage) { tab, position ->
            tab.text = FinancialViewPagerAdapter.TITLE_TABS[position]
        }.attach()
    }
}