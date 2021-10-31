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
                            PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideInstrumentPortfolioFragment(
                                it.first
                            )
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
            setPortfolioTypeChange()
        }
        setColumnListener()
    }

    private fun setPortfolioTypeChange() {
        var tag = binding.awonarPortfolioImageChangeStyle.tag
        if (tag == "market") {
            tag = "manual"
            binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_chart)
            Navigation
                .findNavController(binding.awonarMainDrawerNavigationHostPortfolio)
                .navigate(PortFolioMarketFragmentDirections.portFolioMarketFragmentToPortFolioMaunalFragment())
        } else {
            tag = "market"
            binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list)
            Navigation
                .findNavController(binding.awonarMainDrawerNavigationHostPortfolio)
                .navigate(PortFolioMaunalFragmentDirections.portFolioMaunalFragmentToPortFolioMarketFragment())
        }
        binding.awonarPortfolioImageChangeStyle.tag = tag
        portViewModel.togglePortfolio(tag)
    }

    private fun setColumnListener() {
        binding.awonarPortfolioTextColumnOne.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnOne.tag
            portViewModel.sortColumn(
                binding.awonarPortfolioTextColumnOne.text.toString(),
                tag == "DESC"
            )
            binding.awonarPortfolioTextColumnOne.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnTwo.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnTwo.tag
            portViewModel.sortColumn(
                binding.awonarPortfolioTextColumnTwo.text.toString(),
                tag == "DESC"
            )
            binding.awonarPortfolioTextColumnTwo.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnThree.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnThree.tag
            portViewModel.sortColumn(
                binding.awonarPortfolioTextColumnThree.text.toString(),
                tag == "DESC"
            )
            binding.awonarPortfolioTextColumnThree.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnFore.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnFore.tag
            portViewModel.sortColumn(
                binding.awonarPortfolioTextColumnFore.text.toString(),
                tag == "DESC"
            )
            binding.awonarPortfolioTextColumnFore.tag = if (tag == "DESC") "ASC" else "DESC"
        }
    }

}