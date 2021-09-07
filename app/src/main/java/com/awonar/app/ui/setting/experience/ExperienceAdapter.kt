package com.awonar.app.ui.setting.experience

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.awonar.android.model.experience.ExperienceResponse
import com.awonar.app.ui.setting.experience.page.TradingExperienceFragment
import com.awonar.app.ui.setting.experience.page.PlannedInvestmentsExperienceFragment
import com.awonar.app.ui.setting.experience.page.TradingKnowledgeExperienceFragment
import com.awonar.app.ui.setting.experience.page.PurposeTradingExperienceFragment
import java.lang.Error

class ExperienceAdapter constructor(
    private val questions: ExperienceResponse,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = questions.topics?.size ?: 0

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TradingExperienceFragment.newInstance(questions.topics?.get(position))
        1 -> TradingKnowledgeExperienceFragment.newInstance(questions.topics?.get(position))
        2 -> PlannedInvestmentsExperienceFragment.newInstance(questions.topics?.get(position))
        3 -> PurposeTradingExperienceFragment.newInstance(questions.topics?.get(position))
        4 -> PurposeTradingExperienceFragment.newInstance(questions.topics?.get(position))
        else -> throw Error("View Pager is not found with position $position")
    }
}