package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentAllFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.ui.feed.adapter.FeedsAdapter
import com.awonar.app.utils.DateUtils
import com.awonar.app.widget.feed.DefaultFeed
import com.awonar.app.widget.feed.PreviewFeed
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class AllFeedFragment : Fragment() {

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
            viewModel.feedsState.collect { feeds ->
                with(binding.awonrAllFeedRecycler) {
                    if (adapter == null) {
                        adapter = FeedsAdapter()
                        layoutManager =
                            LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false)
                    }
                    val itemList = mutableListOf<FeedItem>()
                    feeds.forEach {
                        itemList.add(FeedItem.DefaultFeed(
                            avatar = it?.user?.picture,
                            title = it?.user?.username,
                            subTitle = DateUtils.getTimeAgo(it?.createdAt),
                            description = it?.description
                        ))
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
}