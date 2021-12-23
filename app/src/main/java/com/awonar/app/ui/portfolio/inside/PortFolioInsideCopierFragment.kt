package com.awonar.app.ui.portfolio.inside

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideCopierBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioInsideCopierFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val positionViewModel: PositionViewModel by activityViewModels()
    private val insideViewModel: PositionInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val args: PortFolioInsideCopierFragmentArgs by navArgs()

    private var currentIndex: Int = 0

    private val binding: AwonarFragmentPortfolioInsideCopierBinding by lazy {
        AwonarFragmentPortfolioInsideCopierBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                portFolioViewModel.copierState.collect {
                    marketViewModel.getConversionsRateList(it?.positions ?: emptyList())
                    columnsViewModel.getActivedColumns()
                }
            }

            launch {
                columnsViewModel.activedColumnState.collect { newColumn ->
                    if (newColumn.isNotEmpty()) {
                        binding.column1 = newColumn[0]
                        binding.column2 = newColumn[1]
                        binding.column3 = newColumn[2]
                        binding.column4 = newColumn[3]
                    }
                }
            }
        }
        binding.viewModel = insideViewModel
        binding.positionViewModel = positionViewModel
        binding.columnsViewModel = columnsViewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentIndex = args.index
        columnsViewModel.getActivedColumns()
        insideViewModel.convertCopies(portFolioViewModel.positionState.value, currentIndex)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.awonarPortfolioInsideInstrumentToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.awonarPortfolioInsideInstrumentToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.awonar_menu_instide_position_back -> {
                    if (currentIndex > 0) {
                        currentIndex--
                        insideViewModel.convertCopies(portFolioViewModel.positionState.value,
                            currentIndex)
                    }
                    true
                }
                R.id.awonar_menu_instide_position_next -> {
                    val positionSize = portFolioViewModel.positionState.value?.copies?.size ?: 0
                    if (currentIndex < positionSize.minus(1)) {
                        currentIndex++
                        insideViewModel.convertCopies(portFolioViewModel.positionState.value,
                            currentIndex)
                    }
                    true
                }
                else -> false
            }
        }
    }

}