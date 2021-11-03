package com.awonar.app.ui.portfolio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPortfolioBinding
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class PortFolioFragment : Fragment() {

    private val portViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val tag = binding.awonarPortfolioImageChangeStyle.tag
                portViewModel.getActivedColoumn("$tag")
            }
        }

    private val binding: AwonarFragmentPortfolioBinding by lazy {
        AwonarFragmentPortfolioBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            portViewModel.navigateInsideInstrumentPortfolio.collect {
                when (it.second) {
                    "instrument" -> findNavController()
                        .navigate(
                            PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideInstrumentPortfolioFragment()
                                .apply {
                                    positionId = it.first
                                }
                        )
                    "copier" -> findNavController()
                        .navigate(
                            PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideCopierPortfolioFragment(
                                it.first
                            )
                        )
                }

            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                portViewModel.portfolioType.collect {
                    portViewModel.getActivedColoumn(it)
                }
            }
            launch {
                portViewModel.subscricbeQuote.collect { instrumentIds ->
                    instrumentIds.forEach {
                        marketViewModel.subscribe(it)
                    }
                }
            }
            launch {
                portViewModel.activedColumnState.collect { newColumn ->
                    if (newColumn.isNotEmpty()) {
                        binding.column1 = newColumn[0]
                        binding.column2 = newColumn[1]
                        binding.column3 = newColumn[2]
                        binding.column4 = newColumn[3]
                    }
                }
            }
        }
        binding.viewModel = portViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marketViewModel.setNewQuoteListener()
        binding.awonarPortfolioImageIconList.setOnClickListener {
            val tag = binding.awonarPortfolioImageChangeStyle.tag
            openActivityCompatForResult(
                activityResult, PortFolioColumnActivedActivity::class.java, bundleOf(
                    PortFolioColumnActivedActivity.EXTRA_PORTFOLIO_TYPE to tag
                )
            )
        }
        binding.awonarPortfolioImageChangeStyle.setOnClickListener {
            toggle()
        }
        setColumnListener()
    }

    private fun toggle() {
        var tag = binding.awonarPortfolioImageChangeStyle.tag
        val action: NavDirections = when (tag) {
            "market" -> {
                tag = "manual"
                binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_chart)
                PortFolioMarketFragmentDirections.portFolioMarketFragmentToPortFolioMaunalFragment()
            }
            "manual" -> {
                tag = "card"
                binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list)
                PortFolioMaunalFragmentDirections.portFolioMaunalFragmentToPortFolioCardFragment()
            }
            "card" -> {
                tag = "piechart"
                PortFolioCardFragmentDirections.portFolioCardFragmentToPortFolioPieChartFragment()
            }
            else -> {
                tag = "market"
                PortfolioPieChartFragmentDirections.portfolioPieChartFragmentToPortFolioMarketFragment()
            }
        }
        Navigation
            .findNavController(binding.awonarPortfolioNavigationHostPortfolio)
            .navigate(action)
        binding.awonarPortfolioImageChangeStyle.tag = tag
        portViewModel.togglePortfolio(tag)
    }

    private fun setColumnListener() {
        binding.awonarPortfolioIncludeColumn.apply {
            awonarIncludeTextColumnOne.setOnClickListener {
                val tag = awonarIncludeTextColumnOne.tag
                portViewModel.sortColumn(
                    awonarIncludeTextColumnOne.text.toString(),
                    tag == "DESC"
                )
                awonarIncludeTextColumnOne.tag = if (tag == "DESC") "ASC" else "DESC"
            }
            awonarIncludeTextColumnTwo.setOnClickListener {
                val tag = awonarIncludeTextColumnTwo.tag
                portViewModel.sortColumn(
                    awonarIncludeTextColumnTwo.text.toString(),
                    tag == "DESC"
                )
                awonarIncludeTextColumnTwo.tag = if (tag == "DESC") "ASC" else "DESC"
            }
            awonarIncludeTextColumnThree.setOnClickListener {
                val tag = awonarIncludeTextColumnThree.tag
                portViewModel.sortColumn(
                    awonarIncludeTextColumnThree.text.toString(),
                    tag == "DESC"
                )
                awonarIncludeTextColumnThree.tag = if (tag == "DESC") "ASC" else "DESC"
            }
            awonarIncludeTextColumnFour.setOnClickListener {
                val tag = awonarIncludeTextColumnFour.tag
                portViewModel.sortColumn(
                    awonarIncludeTextColumnFour.text.toString(),
                    tag == "DESC"
                )
                awonarIncludeTextColumnFour.tag = if (tag == "DESC") "ASC" else "DESC"
            }
        }
    }

}