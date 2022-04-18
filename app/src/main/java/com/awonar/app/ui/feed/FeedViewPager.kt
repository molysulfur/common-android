package com.awonar.app.ui.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FeedViewPager constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> AllFeedFragment()
        1 -> FollowingFeedFragment()
        2 -> CopyTradeFeedFragment()
        else -> throw Error("View Pager is not found!")
    }

}