package com.awonar.app.ui.marketprofile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awonar.app.R
import com.awonar.app.ui.marketprofile.about.MarketAboutFragment
import com.awonar.app.ui.profile.ProfilePrivateFragment
import com.awonar.app.ui.profile.StatisticProfileFragment
import com.awonar.app.ui.profile.user.UserPortfolioFragment
import com.awonar.app.ui.user.UserInfoFragment

class MarketProfilePagerAdapter constructor(
    fragmentManager: FragmentManager,
    lifecycler: Lifecycle,
) :
    FragmentStateAdapter(fragmentManager, lifecycler) {

    companion object {
        val ICON_TABS = arrayListOf(
            R.drawable.awonar_ic_timeline,
            R.drawable.awonar_ic_about,
            R.drawable.awonar_ic_statistics,
            R.drawable.awonar_ic_portfolio,
        )
    }


    override fun getItemCount(): Int = ICON_TABS.size

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ProfilePrivateFragment()
        1 -> MarketAboutFragment.newInstance()
        else -> ProfilePrivateFragment()
    }
}