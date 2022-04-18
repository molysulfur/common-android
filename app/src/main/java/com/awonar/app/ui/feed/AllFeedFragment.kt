package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentAllFeedBinding
import com.awonar.app.widget.feed.DefaultFeed
import com.awonar.app.widget.feed.PreviewFeed

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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = PreviewFeed(requireContext()).apply {
            title = "Name 2"
            subTitle = "1 hour ago"
            description = "this is sub feed"
        }
        with(binding.defaultFeed) {
            title = "Name"
            subTitle = "1 hour ago"
            description = "this is feed"
            addOptionView(view)
        }
    }
}