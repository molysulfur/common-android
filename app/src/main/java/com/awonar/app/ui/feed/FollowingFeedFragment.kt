package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentFollowingFeedBinding
import com.awonar.app.ui.feed.adapter.FeedsAdapter
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle

class FollowingFeedFragment : Fragment() {

    private val binding: AwonarFragmentFollowingFeedBinding by lazy {
        AwonarFragmentFollowingFeedBinding.inflate(layoutInflater)
    }

    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.feedItems.collect { itemList ->
                with(binding.awonarFollowingFeedRecycler) {
                    if (adapter == null) {
                        adapter = FeedsAdapter().apply {
                            onLoadMore = {
                                viewModel.loadMore()
                            }
                        }
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                    (adapter as FeedsAdapter).itemLists = itemList
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}