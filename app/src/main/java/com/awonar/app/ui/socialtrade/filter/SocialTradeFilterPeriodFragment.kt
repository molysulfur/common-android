package com.awonar.app.ui.socialtrade.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.constrant.*
import com.awonar.app.databinding.AwonarFragmentSocialTradeFilterPeriodBinding

class SocialTradeFilterPeriodFragment : Fragment() {

    private val viewModel: SocialTradeFilterViewModel by activityViewModels()

    private val args: SocialTradeFilterPeriodFragmentArgs by navArgs()

    private val binding: AwonarFragmentSocialTradeFilterPeriodBinding by lazy {
        AwonarFragmentSocialTradeFilterPeriodBinding.inflate(layoutInflater)
    }

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
        when (args.filterType) {
            "period" -> viewModel.setFilterList(args.filterType, timePeriodsData)
            "status" -> viewModel.setFilterList(args.filterType, timeStatusData)
            "allocation" -> viewModel.setFilterList(args.filterType, allocationData)
        }


        binding.awonarSocialTradeFilterSelectorButtonApply.setOnClickListener {
            viewModel.save(args.filterType)
            findNavController().popBackStack()
        }
    }
}