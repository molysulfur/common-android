package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentFeedBinding
import com.awonar.app.ui.feed.FeedViewPager.Companion.TAB_TITLE
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

class FeedFragment : Fragment() {

    private val binding: AwonarFragmentFeedBinding by lazy {
        AwonarFragmentFeedBinding.inflate(layoutInflater)
    }

    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.awonarFeedRefreshUpdate) {
            setOnRefreshListener {
                viewModel.refresh()
                isRefreshing = false
            }
        }
        with(binding.awonarFeedViewpagerFeed) {
            adapter = FeedViewPager(childFragmentManager, lifecycle)
        }
        with(binding.awonarFeedTabsFeeds) {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val index = TAB_TITLE.indexOf(tab?.text)
                    when (index) {
                        0 -> viewModel.setFeedType()
                        1 -> viewModel.setFeedType("following")
                        2 -> viewModel.setFeedType("copy")
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    viewModel.refresh()
                    viewModel.goToTop()
                }

            })
        }
        TabLayoutMediator(
            binding.awonarFeedTabsFeeds,
            binding.awonarFeedViewpagerFeed
        ) { tab, index ->
            tab.text = TAB_TITLE[index]
        }.attach()

    }
}