package com.awonar.app.ui.portfolio.inside

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideInstrumentBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.order.OrderDialog
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.ui.order.edit.EditPositionDialog
import com.awonar.app.ui.order.partialclose.PartialCloseDialog
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.ui.publishfeed.PublishFeedActivity
import com.google.android.material.snackbar.Snackbar
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.launch

class PortFolioInsideInstrumentFragment : Fragment() {

    companion object {
        private const val OPEN_DIALOG_TAG =
            "com.awonar.app.ui.portfolio.inside.dialog.open_position"
    }

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val activityViewModel: PositionInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val positionViewModel: PositionViewModel by activityViewModels()

    private val args: PortFolioInsideInstrumentFragmentArgs by navArgs()

    private var menus: ArrayList<MenuDialog> = arrayListOf(
        MenuDialog(key = "new_trade", text = "Open New Trade"),
        MenuDialog(key = "new_post", text = "Write New Post"),
        MenuDialog(key = "view_chart", text = "View Chart"),
        MenuDialog(key = "set_price_alert", text = "Set Price Alert"),
    )

    private lateinit var settingBottomSheet: MenuDialogButtonSheet


    private val binding: AwonarFragmentPortfolioInsideInstrumentBinding by lazy {
        AwonarFragmentPortfolioInsideInstrumentBinding.inflate(layoutInflater)
    }

    private var currentIndex: Int = 0

    private lateinit var helper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            activityViewModel.editDialog.collect { position ->
                position?.let {
                    EditPositionDialog.Builder()
                        .setPosition(it)
                        .setKey("inside_instrument")
                        .build()
                        .show(childFragmentManager)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            activityViewModel.closeDialog.collect { position ->
                position?.let {
                    PartialCloseDialog.Builder()
                        .setPosition(it)
                        .setKey("inside_instrument")
                        .build()
                        .show(childFragmentManager)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                orderViewModel.orderSuccessState.collect {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.awonar_color_green
                            )
                        ).show()
                }
            }
            launch {
                activityViewModel.editDialog.collect { position ->
                    position?.let {
                        EditPositionDialog.Builder()
                            .setPosition(it)
                            .build()
                            .show(childFragmentManager)
                    }
                }
            }
            launch {
                columnsViewModel.activedColumnState.collect { newColumn ->
                    binding.column1 = newColumn[0]
                    binding.column2 = newColumn[1]
                    binding.column3 = newColumn[2]
                    binding.column4 = newColumn[3]
                }
            }
        }
        binding.viewModel = activityViewModel
        binding.columnsViewModel = columnsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupListener()
        currentIndex = args.position
        columnsViewModel.setColumnType("manual")
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val item =
                    positionViewModel.positionItems.value[currentIndex] as PortfolioItem.PositionItem
                val position: List<Position> = item.instrumentGroup ?: emptyList()
                if (position.isNotEmpty()) {
                    val quote = quotes[position[0].instrument?.id]
                    quote?.let {
                        setupHeader(quote, position, item.conversionRate)
                    }
                }
            }
        }
        activityViewModel.convertPosition(
            portFolioViewModel.positionState.value,
            positionViewModel.positionItems.value[currentIndex] as PortfolioItem.PositionItem
        )
        setupToolbar()
        setTouchHelper()
        setupListener()
    }

    private fun setupDialog() {
        settingBottomSheet = MenuDialogButtonSheet.Builder()
            .setListener(object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
                override fun onMenuClick(menu: MenuDialog) {
                    val item =
                        positionViewModel.positionItems.value[currentIndex] as PortfolioItem.PositionItem
                    val position: Position? = item.instrumentGroup?.get(0)
                    when (menu.key) {
                        "new_trade" -> OrderDialog.Builder()
                            .setType(true)
                            .setSymbol(instrument = position?.instrument)
                            .build()
                            .show(childFragmentManager)
                        "new_post" -> openActivity(PublishFeedActivity::class.java)
                    }
                }
            })
            .setMenus(menus)
            .build()
    }

    private suspend fun setupHeader(
        quote: Quote,
        position: List<Position>,
        conversionRate: Float
    ) {
        val price = quote.close
        val change = ConverterQuoteUtil.change(quote.close, quote.previous)
        val percent = ConverterQuoteUtil.percentChange(quote.previous, quote.close)
        val invested = position.sumOf { it.amount.toDouble() }
        var totalValue = 0.0
        val totalPL = position.sumOf {
            val currentPl = PortfolioUtil.getProfitOrLoss(
                ConverterQuoteUtil.getCurrentPrice(
                    quote,
                    it.leverage,
                    it.isBuy
                ),
                it.openRate,
                it.units,
                conversionRate,
                it.isBuy
            ).toDouble()
            val value = currentPl.plus(it.amount)
            totalValue += value
            currentPl
        }
        val sumBuy = position.filter { it.isBuy }.sumOf { it.units.times(it.openRate).toDouble() }
        val sumSell = position.filter { !it.isBuy }.sumOf { it.units.times(it.openRate).toDouble() }
        val sumUnitBuy = position.filter { it.isBuy }.sumOf { it.units.toDouble() }
        val sumUnitSell = position.filter { !it.isBuy }.sumOf { it.units.toDouble() }
        val avgOpen = sumBuy.minus(sumSell).div((sumUnitBuy.minus(sumUnitSell)))
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.apply {
            setImage(position[0].instrument?.logo ?: "")
            setTitle(position[0].instrument?.symbol ?: "")
            setPrice(price)
            setChange(change)
            setChangePercent(percent)
            setStatusText(quote.status ?: "")
            setInvested(invested.toFloat())
            setValueInvested(totalValue.toFloat())
            setProfitLoss(totalPL.toFloat())
            setAvgOpen(avgOpen.toFloat())
            setUnit(sumUnitBuy.plus(sumUnitSell).toFloat())
        }
        binding.awonarPortfolioButtonBuy.text = "${quote.bid}"
        binding.awonarPortfolioButtonSell.text = "${quote.ask}"
    }

    private fun setupListener() {
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.onSetting = {
            settingBottomSheet.show(childFragmentManager, OPEN_DIALOG_TAG)
        }
        binding.awonarPortfolioButtonBuy.setOnClickListener {
            val item =
                positionViewModel.positionItems.value[currentIndex] as PortfolioItem.PositionItem
            val position: Position? = item.instrumentGroup?.get(0)
            OrderDialog.Builder().setSymbol(position?.instrument).setType(true).build()
                .show(childFragmentManager)
        }
        binding.awonarPortfolioButtonSell.setOnClickListener {
            val item =
                positionViewModel.positionItems.value[currentIndex] as PortfolioItem.PositionItem
            val position: Position? = item.instrumentGroup?.get(0)
            OrderDialog.Builder().setSymbol(position?.instrument).setType(false).build()
                .show(childFragmentManager)
        }
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
//                        activityViewModel.convertPosition(
//                            portFolioViewModel.positionState.value,
//                            currentIndex
//                        )
                    }
                    true
                }
                R.id.awonar_menu_instide_position_next -> {
                    val positionSize = portFolioViewModel.positionState.value?.positions?.size ?: 0
                    if (currentIndex < positionSize.minus(1)) {
                        currentIndex++
//                        activityViewModel.convertPosition(
//                            portFolioViewModel.positionState.value,
//                            currentIndex
//                        )
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setTouchHelper() {
        val touchHelperCallback = PortfolioListItemTouchHelperCallback(
            object : IPortfolioListItemTouchHelperCallback {
                override fun onClick(position: Int) {
                    activityViewModel.showEditDialog(position)
                }

                override fun onClose(position: Int) {
                    activityViewModel.showCloseDialog(position)
                }
            },
            requireContext()
        )
        helper = ItemTouchHelper(touchHelperCallback)
        binding.awonarPortfolioRecyclerContainer.apply {
            helper.attachToRecyclerView(this)
            addItemDecoration(object :
                RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    super.onDraw(c, parent, state)
                    touchHelperCallback.onDraw(c)
                }
            })
        }
    }
}