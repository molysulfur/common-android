package com.awonar.app.ui.marketprofile.stat.financial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awonar.app.R
import com.awonar.app.ui.marketprofile.about.MarketAboutFragment
import com.awonar.app.ui.marketprofile.stat.MarketStatisticFragment
import com.awonar.app.ui.profile.ProfilePrivateFragment


class FinancialViewPagerAdapter constructor(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        val TITLE_TABS = arrayListOf(
           "Statistic",
            "Income Statement",
            "Balance Sheet",
            "Cashflow"
        )
    }


    override fun getItemCount(): Int = TITLE_TABS.size

    override fun createFragment(position: Int): Fragment = when (position) {
        1 -> MarketAboutFragment.newInstance()
        3 -> MarketStatisticFragment()
        else -> ProfilePrivateFragment()
    }
}