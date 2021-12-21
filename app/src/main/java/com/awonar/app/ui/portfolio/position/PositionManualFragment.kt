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
import com.awonar.app.databinding.AwonarFragmentPositionManualBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.order.edit.OrderEditDialog
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.IPortfolioListItemTouchHelperCallback
import com.awonar.app.ui.portfolio.adapter.PortfolioListItemTouchHelperCallback
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PositionManualFragment : Fragment() {

    private val binding: AwonarFragmentPositionManualBinding by lazy {
        AwonarFragmentPositionManualBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val columns: ColumnsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        launchAndRepeatWithViewLifecycle {
//            viewModel.editDialog.collect { position ->
//                position?.let {
//                    OrderEditDialog.Builder()
//                        .setPosition(position = it)
//                        .build()
//                        .show(parentFragmentManager)
//                }
//            }
//        }
        binding.columns = columns
//        binding.viewModel = viewModel
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
                        viewModel.showEditDialog(position)
                    }
                }

                override fun onClose(position: Int) {
                }
            },
            requireContext()
        )
        val helper = ItemTouchHelper(touchHelperCallback)
        helper.attachToRecyclerView(binding.awonarPositionManualRecycler)
        binding.awonarPositionManualRecycler.addItemDecoration(object :
            RecyclerView.ItemDecoration() {

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                touchHelperCallback.onDraw(c)
            }
        })
    }

}