package com.awonar.app.ui.portfolio.position

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPositionCardBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel

class PositionCardFragment : Fragment() {

    private val binding: AwonarFragmentPositionCardBinding by lazy {
        AwonarFragmentPositionCardBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val columns: ColumnsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.columns = columns
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCardPosition()
    }
}