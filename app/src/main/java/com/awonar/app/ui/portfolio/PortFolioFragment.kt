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
                    portfolio?.let {
                        portViewModel.sumTotalProfit(it, quotes)
                        portViewModel.sumTotalEquity(it)
                    }
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
                portViewModel.profitState.collect {
                    binding.awonarPortfolioTextTitleProfit.setTextColor(ColorChangingUtil.getTextColorChange(
                        requireContext(),
                        it))
                    binding.awonarPortfolioTextTitleProfit.text =
                        "Profit : \$%.2f".format(it)
                }
            }
            launch {
                portViewModel.equityState.collect {
                    binding.awonarPortfolioTextTitleEquity.setTextColor(ColorChangingUtil.getTextColorChange(
                        requireContext(),
                        it))
                    binding.awonarPortfolioTextTitleEquity.text =
                        "Equity : \$%.2f".format(it)
                }
            }
            launch {
                portViewModel.portfolioState.collect {
                    portfolio = it
                    binding.awonarPortfolioTextTitleAvailable.text =
                        "Available : \$%.2f".format(portfolio?.available ?: 0f)
                    binding.awonarPortfolioTextTitleTotalAllocate.text =
                        "Allocate : \$%.2f".format(portfolio?.totalAllocated ?: 0f)
                }
            }
            launch {
                portViewModel.portfolioType.collect {
//                    binding.awonarPortfolioImageChangeStyle.tag = it
//                    fetchPosition(it)
//                    visibleColumns(it)
//                    setupPositionType(it)
//                    columnsViewModel.setColumnType(it)
                }
            }
//            launch {
//                portViewModel.subscricbeQuote.collect { instrumentIds ->
//                    instrumentIds.forEach {
//                        marketViewModel.subscribe(it)
//                    }
//                }
//            }
        }
        binding.viewModel = portViewModel
        binding.columnViewModel = columnsViewModel
        binding.market = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

//    private fun setupPositionType(type: String) {
//        val iconRes = when (type) {
//            "market" -> R.drawable.awonar_ic_list
//            "manual" -> R.drawable.awonar_ic_card_list
//            "card" -> R.drawable.awonar_ic_chart
//            "piechart" -> R.drawable.awonar_ic_list
//            else -> 0
//        }
//        binding.awonarPortfolioImageChangeStyle.setImageResource(iconRes)
//    }
//
//    private fun visibleColumns(type: String) {
//        helper.attachToRecyclerView(null)
//        when (type) {
//            "market" -> portViewModel.getMarketPosition()
//            "manual" -> {
//                portViewModel.getManualPosition()
//                helper.attachToRecyclerView(binding.awonarPortfolioRecyclerPosition)
//            }
//            "card" -> portViewModel.getCardPosition()
//            "piechart" -> portViewModel.getExposure()
//        }
//    }

//    private fun fetchPosition(type: String) {
//        val visible = when (type) {
//            "market" -> View.VISIBLE
//            "manual" -> View.VISIBLE
//            "card" -> View.GONE
//            "piechart" -> View.GONE
//            else -> View.GONE
//        }
//        binding.awonarPortfolioIncludeColumn.awonarIncludeColumnContainer.visibility = visible
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
//        setTouchHelper()
//        binding.awonarPortfolioImageChangeStyle.tag = "market"
//        columnsViewModel.setColumnType("${binding.awonarPortfolioImageChangeStyle.tag}")
//        portViewModel.getMarketPosition()
//        marketViewModel.subscribe()
//        binding.awonarPortfolioTextTitleSection.setOnClickListener {
//            if (sectorDialog.isAdded) {
//                sectorDialog.dismiss()
//            }
//            sectorDialog.show(childFragmentManager, MenuDialogButtonSheet.TAG)
//        }
//        binding.awonarPortfolioImageIconList.setOnClickListener {
//            val tag = binding.awonarPortfolioImageChangeStyle.tag
//            openActivityCompatForResult(
//                activityResult, ColumnsActivedActivity::class.java, bundleOf(
//                    ColumnsActivedActivity.EXTRA_COLUMNS_ACTIVED to tag
//                )
//            )
//        }
//        binding.awonarPortfolioImageChangeStyle.setOnClickListener {
//            toggle()
//        }
//        setColumnListener()
    }

    private fun setupDialog() {
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
//        sectorDialog = MenuDialogButtonSheet.Builder()
//            .setListener(object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
//                override fun onMenuClick(menu: MenuDialog) {
//                    var title = binding.awonarPortfolioTextTitleSection.text
//                    when (menu.key) {
//                        "com.awonar.app.ui.portfolio.sector.orders" -> {
//                            title = "Orders"
//                            portViewModel.getOrdersPosition()
//                        }
//                        "com.awonar.app.ui.portfolio.sector.history" -> {
//                            findNavController().navigate(PortFolioFragmentDirections.actionPortFolioFragmentToHistoryFragment())
//                        }
//                        else -> {
//                        }
//                    }
//                    binding.awonarPortfolioTextTitleSection.text = title
//                }
//            })
//            .setMenus(menus)
//            .build()
    }

//    private fun toggle() {
//        var tag = binding.awonarPortfolioImageChangeStyle.tag
//        tag = when (tag) {
//            "market" -> "manual"
//            "manual" -> "card"
//            "card" -> "piechart"
//            else -> "market"
//        }
//        portViewModel.togglePortfolio(tag)
//    }
//
//    private fun setColumnListener() {
//        binding.awonarPortfolioIncludeColumn.apply {
//            awonarIncludeTextColumnOne.setOnClickListener {
//                val tag = awonarIncludeTextColumnOne.tag
//                columnsViewModel.sortColumn(
//                    awonarIncludeTextColumnOne.text.toString(),
//                    tag == "DESC"
//                )
//                awonarIncludeTextColumnOne.tag = if (tag == "DESC") "ASC" else "DESC"
//            }
//            awonarIncludeTextColumnTwo.setOnClickListener {
//                val tag = awonarIncludeTextColumnTwo.tag
//                columnsViewModel.sortColumn(
//                    awonarIncludeTextColumnTwo.text.toString(),
//                    tag == "DESC"
//                )
//                awonarIncludeTextColumnTwo.tag = if (tag == "DESC") "ASC" else "DESC"
//            }
//            awonarIncludeTextColumnThree.setOnClickListener {
//                val tag = awonarIncludeTextColumnThree.tag
//                columnsViewModel.sortColumn(
//                    awonarIncludeTextColumnThree.text.toString(),
//                    tag == "DESC"
//                )
//                awonarIncludeTextColumnThree.tag = if (tag == "DESC") "ASC" else "DESC"
//            }
//            awonarIncludeTextColumnFour.setOnClickListener {
//                val tag = awonarIncludeTextColumnFour.tag
//                columnsViewModel.sortColumn(
//                    awonarIncludeTextColumnFour.text.toString(),
//                    tag == "DESC"
//                )
//                awonarIncludeTextColumnFour.tag = if (tag == "DESC") "ASC" else "DESC"
//            }
//        }
//    }
//


}