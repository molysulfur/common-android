package com.awonar.app.ui.profile.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentHistoryPortfolioBinding

class HistoryPositionFragment : Fragment() {
    private val binding: AwonarFragmentHistoryPortfolioBinding by lazy {
        AwonarFragmentHistoryPortfolioBinding.inflate(layoutInflater)
    }
    private var dateType = "30D"

    private val viewModel: HistoryProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.column1 = "Traders"
        binding.column2 = "Profitable"
        binding.column3 = "P/L(%)"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistoryPosition()
    }

}