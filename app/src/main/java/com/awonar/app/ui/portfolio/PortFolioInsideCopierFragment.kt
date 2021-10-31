package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideCopierBinding
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioInsideCopierFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val args: PortFolioInsideCopierFragmentArgs by navArgs()

    private val binding: AwonarFragmentPortfolioInsideCopierBinding by lazy {
        AwonarFragmentPortfolioInsideCopierBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            portFolioViewModel.navigateInsideInstrumentPortfolio.collect {
                when (it.second) {
                    "instrument" -> findNavController()
                        .navigate(
                            PortFolioInsideCopierFragmentDirections.actionPortFolioInsideCopierFragmentToPortFolioInsideInstrumentProfileFragment(
                                it.first
                            )
                        )
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                portFolioViewModel.activedColumnState.collect { newColumn ->
                }
            }
            launch {
                portFolioViewModel.copierState.collect {
                    portFolioViewModel.getActivedColoumn("market")
                }
            }

            launch {
                portFolioViewModel.activedColumnState.collect { newColumn ->
                    if (newColumn.isNotEmpty()) {
                        binding.column1 = newColumn[0]
                        binding.column2 = newColumn[1]
                        binding.column3 = newColumn[2]
                        binding.column4 = newColumn[3]
                    }
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
            portFolioViewModel.getCopierPosition(it)
        }
    }
}