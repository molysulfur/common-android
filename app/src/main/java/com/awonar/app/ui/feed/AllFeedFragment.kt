package com.awonar.app.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentAllFeedBinding

class AllFeedFragment : Fragment() {

    private val binding: AwonarFragmentAllFeedBinding by lazy {
        AwonarFragmentAllFeedBinding.inflate(layoutInflater)
    }

    private val viewModel : FeedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}