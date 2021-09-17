package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.awonar.app.databinding.AwonarFragmentMarketListBinding
import com.awonar.app.ui.market.adapter.InstrumentHorizontalWrapperAdapter
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.awonar.app.ui.market.adapter.InstrumentListAdapter
import com.awonar.app.ui.market.holder.InstrumentItemViewHolder
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class MarketListFragment constructor() : Fragment() {

    private val binding: AwonarFragmentMarketListBinding by lazy {
        AwonarFragmentMarketListBinding.inflate(layoutInflater)
    }

    private val viewModel: MarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}