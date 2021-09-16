package com.awonar.app.ui.market

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import timber.log.Timber

class MarketPagerViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        val tabLists =
            arrayListOf("Recommended", "Stocks", "Crypto", "Currencies", "ETFs", "Commodity")
    }

    override fun getItemCount(): Int = tabLists.size

    override fun createFragment(position: Int): Fragment {
        Timber.e("$position")
        return when (position) {
            0 -> RecommendedMarketFragment()
            1 -> CategoriesMarketFragment()
            2 -> MarketListFragment()
            3 -> MarketListFragment()
            4 -> MarketListFragment()
            5 -> MarketListFragment()
            else -> throw Error("Market View Pager is not found!")
        }
    }
}