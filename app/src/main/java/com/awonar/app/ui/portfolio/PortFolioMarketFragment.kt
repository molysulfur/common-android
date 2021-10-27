package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioMarketBinding
import com.awonar.app.ui.market.MarketViewModel

class PortFolioMarketFragment : Fragment() {

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val binding: AwonarFragmentPortfolioMarketBinding by lazy {
        AwonarFragmentPortfolioMarketBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marketViewModel.setNewQuoteListener()
    }
}