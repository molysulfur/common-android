package com.awonar.app.ui.portfolio.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPositionChartBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PositionChartFragment : Fragment() {

    private val binding: AwonarFragmentPositionChartBinding by lazy {
        AwonarFragmentPositionChartBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllocate()
    }
}