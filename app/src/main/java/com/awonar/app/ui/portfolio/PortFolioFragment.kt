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
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class PortFolioFragment : Fragment() {

    private val portViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val sectorDialog: MenuDialogButtonSheet by lazy {
        val menus = arrayListOf(
            MenuDialog(
                key = "com.awonar.app.ui.portfolio.sector.history",
                text = "History"
            ),
            MenuDialog(
                key = "com.awonar.app.ui.portfolio.sector.orders",
                text = "Orders"
            )
        )
        MenuDialogButtonSheet.Builder()
            .setListener(object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
                override fun onMenuClick(menu: MenuDialog) {
                    var title = ""
                    when (menu.key) {
                        "com.awonar.app.ui.portfolio.sector.orders" -> {
                            title = "Orders"
                            portViewModel.getOrdersPosition()
//                            portViewModel.togglePortfolio(title)
                        }
                        else -> {
                        }
                    }
                    binding.awonarPortfolioTextTitleSection.text = title
                }
            })
            .setMenus(menus)
            .build()
    }


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
            portViewModel.portfolioState.collect {
                binding.awonarPortfolioTextTitleAvailable.text =
                    "Available : \$%.2f".format(it?.available ?: 0f)
                binding.awonarPortfolioTextTitleTotalAllocate.text =
                    "Allocate : \$%.2f".format(it?.totalAllocated ?: 0f)
            }
        }
        launchAndRepeatWithViewLifecycle {
            portViewModel.navigateInsideInstrumentPortfolio.collect {
                when (it.second) {
                    "instrument" -> findNavController()
                        .navigate(
                            PortFolioFragmentDirections.actionPortFolioFragmentToPortFolioInsideInstrumentPortfolioFragment()
                                .apply {
                                    instrumentId = it.first.toInt()
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
        binding.market = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarPortfolioImageChangeStyle.tag = "market"
        portViewModel.getMarketPosition()
        marketViewModel.subscribe()
        binding.awonarPortfolioTextTitleSection.setOnClickListener {
            sectorDialog.show(childFragmentManager, MenuDialogButtonSheet.TAG)
        }
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
        when (tag) {
            "market" -> {
                tag = "manual"
                binding.awonarPortfolioIncludeColumn.awonarIncludeColumnContainer.visibility =
                    View.VISIBLE
                portViewModel.getManualPosition()
                binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_chart)
            }
            "manual" -> {
                tag = "card"
                binding.awonarPortfolioIncludeColumn.awonarIncludeColumnContainer.visibility =
                    View.GONE
                portViewModel.getCardPosition()
                binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list)
            }
            "card" -> {
                tag = "piechart"
                portViewModel.getAllocate()
                binding.awonarPortfolioIncludeColumn.awonarIncludeColumnContainer.visibility =
                    View.GONE
            }
            else -> {
                tag = "market"
                portViewModel.getMarketPosition()
                binding.awonarPortfolioIncludeColumn.awonarIncludeColumnContainer.visibility =
                    View.VISIBLE
            }
        }
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