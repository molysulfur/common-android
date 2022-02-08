package com.awonar.app.ui.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.awonar.app.R
import com.awonar.app.ui.profile.StatisticProfileFragment
import com.awonar.app.ui.user.UserInfoFragment
import timber.log.Timber

class ProfilePagerAdapter constructor(fragmentManager: FragmentManager, lifecycler: Lifecycle) :
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
            0 -> UserInfoFragment.newInstance()
            1 -> StatisticProfileFragment.newInstance()
            2 -> StatisticProfileFragment.newInstance()
            3 -> StatisticProfileFragment.newInstance()
            else -> StatisticProfileFragment.newInstance()
        }
}