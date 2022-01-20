package com.awonar.app.ui.socialtrade.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.constrant.timePeriods
import com.awonar.app.databinding.AwonarFragmentSocialTradeFilterSelectorBinding

class SocialTradeFilterSelectorFragment : Fragment() {

    private val args: SocialTradeFilterSelectorFragmentArgs by navArgs()

    private val viewModel: SocialTradeFilterViewModel by activityViewModels()

    private val binding: AwonarFragmentSocialTradeFilterSelectorBinding by lazy {
        AwonarFragmentSocialTradeFilterSelectorBinding.inflate(layoutInflater)
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
        fetch()
        binding.awonarSocialTradeFilterSelectorButtonApply.setOnClickListener {
            viewModel.save(args.filterType)
            findNavController().popBackStack()
        }
    }

    private fun fetch() {
        when (args.filterType) {
            "period" -> viewModel.setFilterList(timePeriods)
        }

    }
}