package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentAllFeedBinding
import com.awonar.app.ui.feed.adapter.FeedsAdapter
import com.awonar.app.ui.marketprofile.about.MarketAboutFragment
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class ProfileFeedFragment : Fragment() {

    private val binding: AwonarFragmentAllFeedBinding by lazy {
        AwonarFragmentAllFeedBinding.inflate(layoutInflater)
    }

    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.goToTopState.collectLatest {
                binding.awonrAllFeedRecycler.smoothScrollToPosition(0)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.feedItems.collect { itemList ->
                with(binding.awonrAllFeedRecycler) {
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        fun newInstance(): ProfileFeedFragment = ProfileFeedFragment()
    }

}