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
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderDialog
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.ui.order.edit.OrderEditDialog
import com.awonar.app.ui.order.partialclose.PartialCloseDialog
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.google.android.material.snackbar.Snackbar
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PortFolioInsideInstrumentFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val activityViewModel: PositionInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    private val args: PortFolioInsideInstrumentFragmentArgs by navArgs()

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
                    OrderEditDialog.Builder()
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
                        OrderEditDialog.Builder()
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
        currentIndex = args.index
        columnsViewModel.setColumnType("manual")
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val position: Position? = activityViewModel.positionState.value
                val quote = quotes[position?.instrument?.id]
                quote?.let {
                    setupHeader(quote, position, it)
                }
                updatePositionItem(quotes)
            }
        }
        activityViewModel.convertPosition(portFolioViewModel.positionState.value, currentIndex)
        setupToolbar()
        setTouchHelper()
        setupListener()
    }

    private fun updatePositionItem(quotes: MutableMap<Int, Quote>) {
        val adapter = binding.awonarPortfolioRecyclerContainer.adapter
        if (adapter != null) {
            (adapter as OrderPortfolioAdapter).let {
                it.quote = quotes
            }
        }
    }

    private suspend fun setupHeader(
        quote: Quote,
        position: Position?,
        it: Quote,
    ) {
        val price = ConverterQuoteUtil.getCurrentPrice(
            quote = quote,
            leverage = position?.leverage ?: 1,
            isBuy = position?.isBuy == true
        )
        val change = ConverterQuoteUtil.change(it.close, it.previous)
        val percent = ConverterQuoteUtil.percentChange(it.previous, it.close)
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setPrice(price)
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setChange(change)
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setChangePercent(
            percent)
        binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setStatusText(
            quote.status ?: "")
        binding.awonarPortfolioButtonBuy.text = "${quote.bid}"
        binding.awonarPortfolioButtonSell.text = "${quote.ask}"
        position?.let {
            val profit = orderViewModel.getProfit(price, position)
            val valueInvest = PortfolioUtil.getValue(profit, position.amount)
            binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setValueInvested(
                valueInvest)
            binding.awonarPortfolioInsideInstrumentInstrumentPositionHeader.setProfitLoss(
                profit)
        }
    }

    private fun setupListener() {
        binding.awonarPortfolioButtonBuy.setOnClickListener {
            val position: Position? = activityViewModel.positionState.value
            OrderDialog.Builder().setSymbol(position?.instrument).setType(true).build()
                .show(childFragmentManager)
        }
        binding.awonarPortfolioButtonSell.setOnClickListener {
            val position: Position? = activityViewModel.positionState.value
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
                        activityViewModel.convertPosition(portFolioViewModel.positionState.value,
                            currentIndex)
                    }
                    true
                }
                R.id.awonar_menu_instide_position_next -> {
                    val positionSize = portFolioViewModel.positionState.value?.positions?.size ?: 0
                    if (currentIndex < positionSize.minus(1)) {
                        currentIndex++
                        activityViewModel.convertPosition(portFolioViewModel.positionState.value,
                            currentIndex)
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