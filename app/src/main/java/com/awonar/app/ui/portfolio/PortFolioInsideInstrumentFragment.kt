package com.awonar.app.ui.portfolio

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideInstrumentBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.ui.order.edit.OrderEditDialog
import com.awonar.app.ui.order.partialclose.PartialCloseDialog
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.google.android.material.snackbar.Snackbar
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioInsideInstrumentFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    private val args: PortFolioInsideInstrumentFragmentArgs by navArgs()

    private val binding: AwonarFragmentPortfolioInsideInstrumentBinding by lazy {
        AwonarFragmentPortfolioInsideInstrumentBinding.inflate(layoutInflater)
    }

    private lateinit var helper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    fectchPosition()
                }
            }
            launch {
                portFolioViewModel.editDialog.collect { position ->
                    position?.let {
                        OrderEditDialog.Builder()
                            .setPosition(it)
                            .build()
                            .show(childFragmentManager)
                    }
                }
            }
            launch {
                portFolioViewModel.closeDialog.collect { position ->
                    position?.let {
                        PartialCloseDialog.Builder()
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
            launch {
                portFolioViewModel.positionState.collect {
                    if (it.isNotEmpty()) {
                        columnsViewModel.getActivedColumns()
                        marketViewModel.getConversionsRate(it[0].instrumentId)
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
        columnsViewModel.setColumnType("manual")
        launchAndRepeatWithViewLifecycle {
            marketViewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.find { it.id == args.instrumentId }
                binding.awonarPortfolioButtonBuy.text = "${quote?.bid ?: 0f}"
                binding.awonarPortfolioButtonSell.text = "${quote?.ask ?: 0f}"
            }
        }
        setTouchHelper()
        fectchPosition()
    }

    private fun fectchPosition() {
        args.copier?.let { copier ->
            args.instrumentId.let { instrumentId ->
                portFolioViewModel.getPosition(copier, instrumentId)
            }
        }
        args.instrumentId.let {
            portFolioViewModel.getPosition(it)
        }
    }

    private fun setTouchHelper() {
        val touchHelperCallback = PortfolioListItemTouchHelperCallback(
            object : IPortfolioListItemTouchHelperCallback {
                override fun onClick(position: Int) {
                    if (position >= 0) {
                        portFolioViewModel.showEditDialog(position)
                    }
                }

                override fun onClose(position: Int) {
                    portFolioViewModel.showCloseDialog(position)
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