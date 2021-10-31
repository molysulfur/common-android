package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideInstrumentBinding
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PortFolioInsideInstrumentPortfolioFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val args: PortFolioInsideInstrumentPortfolioFragmentArgs by navArgs()

    private val binding: AwonarFragmentPortfolioInsideInstrumentBinding by lazy {
        AwonarFragmentPortfolioInsideInstrumentBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            portFolioViewModel.positionState.collect {
                if(it.isNotEmpty()){
                    portFolioViewModel.getActivedColoumn("manual")
                    marketViewModel.getConversionsRate(it[0].instrumentId)
                }
            }
        }
        binding.viewModel = portFolioViewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.positionId.let {
            portFolioViewModel.getPosition(it)
        }
    }
}