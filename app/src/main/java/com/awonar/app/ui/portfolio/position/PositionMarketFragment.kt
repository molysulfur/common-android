package com.awonar.app.ui.portfolio.position

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarFragmentPositionMarketBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.PortfolioType
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle

class PositionMarketFragment : Fragment() {

    private val binding: AwonarFragmentPositionMarketBinding by lazy {
        AwonarFragmentPositionMarketBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val activityViewModel: PositionViewModel by activityViewModels()
    private val columns: ColumnsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.positionState.collect { position ->

            }
        }
        binding.columns = columns
        binding.viewModel = activityViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPosition()
        setTouchHelper()
    }

    private fun setTouchHelper() {
        val touchHelperCallback = PortfolioListItemTouchHelperCallback(
            object : IPortfolioListItemTouchHelperCallback {
                override fun onClick(position: Int) {
                    if (position >= 0) {
                    }
                }

                override fun onClose(position: Int) {
                    when (activityViewModel.positionItems.value[position].type) {
                        PortfolioType.INSTRUMENT_PORTFOLIO -> activityViewModel.navigateInstrumentInside(
                            position,
                            "instrument")
                    }

                }
            },
            requireContext()
        )
        val helper = ItemTouchHelper(touchHelperCallback)
        helper.attachToRecyclerView(binding.awonarPositionMarketRecycler)
        binding.awonarPositionMarketRecycler.addItemDecoration(object :
            RecyclerView.ItemDecoration() {

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                touchHelperCallback.onDraw(c)
            }
        })
    }

}