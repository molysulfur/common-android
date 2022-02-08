package com.awonar.app.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentWatchlistListBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class WatchlistRecentlyFragment : Fragment() {

    private val binding: AwonarFragmentWatchlistListBinding by lazy {
        AwonarFragmentWatchlistListBinding.inflate(layoutInflater)
    }
    private val viewModel: WatchlistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.folders.collect {
                viewModel.getWatchlistRecently()
            }
        }
        binding.enableTouch = false
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setTitle("Recently")
    }

}