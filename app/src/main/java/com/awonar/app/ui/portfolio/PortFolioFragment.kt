package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.databinding.AwonarFragmentPortfolioBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.google.android.material.snackbar.Snackbar
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioBinding by lazy {
        AwonarFragmentPortfolioBinding.inflate(layoutInflater)
    }

    private var portfolio: Portfolio? = null

    private val portViewModel: PortFolioViewModel by activityViewModels()
    private val positionViewModel: PositionViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            positionViewModel.navigateActions.collect {
                Navigation.findNavController(binding.awonarMainDrawerNavigationHostPortfolio)
                    .navigate(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                QuoteSteamingManager.quotesState.collect { quotes ->
                    portViewModel.sumTotalProfitAndEquity(quotes)
                }
            }
            launch {
                orderViewModel.orderSuccessState.collect {
                    Snackbar.make(binding.root, "Successfully.", Snackbar.LENGTH_SHORT).show()
                }
            }
            launch {
                columnsViewModel.sortColumnState.collect { sort ->
//                    (binding.awonarPortfolioRecyclerPosition.adapter as? OrderPortfolioAdapter)?.let {
//                        it.sortColumn(sort.first, sort.second)
//                    }
                }
            }
            launch {
                columnsViewModel.activedColumnState.collect {
                    if (it.size >= 4) {
                        binding.column1 = it[0]
                        binding.column2 = it[1]
                        binding.column3 = it[2]
                        binding.column4 = it[3]
                    }
                }
            }
            launch {
                portViewModel.portfolioState.collect {
                    portfolio = it
                }
            }
        }
        binding.viewModel = portViewModel
        binding.columnViewModel = columnsViewModel
        binding.market = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}