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
import com.awonar.app.dialog.copier.CopierDialog
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioInsideCopierFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
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
            portFolioViewModel.navigateInsideInstrumentPortfolio.collect { pair ->
                val copier = portFolioViewModel.copierState.value
                copier?.let {
                    val position = copier.positions?.find { it.id == pair.first }
                    when (pair.second) {
                        "instrument" -> findNavController()
                            .navigate(
                                PortFolioInsideCopierFragmentDirections.actionPortFolioInsideCopierFragmentToPortFolioInsideInstrumentProfileFragment()
                                    .apply {
                                        this.copier = copier.id
                                        this.instrumentId = position?.instrumentId ?: 0
                                    }
                            )
                    }
                }

            }
        }
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
        binding.viewModel = portFolioViewModel
        binding.columnsViewModel = columnsViewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        columnsViewModel.setColumnType("market")
        args.positionId.let {
            portFolioViewModel.getCopierPosition(it)
        }
        binding.awonarPortfolioInsideCopierPositionHeader.onSetting = {
            val copier = portFolioViewModel.copierState.value
            CopierDialog.Builder()
                .setCopiesId(copier?.user?.id)
                .build()
                .show(childFragmentManager)
        }
    }
}