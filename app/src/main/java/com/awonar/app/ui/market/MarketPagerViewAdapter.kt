package com.awonar.app.ui.market

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MarketPagerViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        val tabLists =
            arrayListOf("Recommended", "Stocks", "Crypto", "Currencies", "ETFs", "Commodity")
    }

    override fun getItemCount(): Int = tabLists.size

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> RecommendedMarketFragment()
        else -> CategoriesMarketFragment()
    }
}