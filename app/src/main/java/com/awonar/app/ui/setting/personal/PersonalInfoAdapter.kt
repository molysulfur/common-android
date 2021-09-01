package com.awonar.app.ui.setting.personal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.Error

class PersonalInfoAdapter constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> PersonalInfoStepOneFragment.newInstance()
        1 -> PersonalInfoStepTwoFragment.newInstance()
        2 -> PersonalInfoStepThreeFragment.newInstance()
        else -> throw Error("View Pager is not found with position $position")
    }
}