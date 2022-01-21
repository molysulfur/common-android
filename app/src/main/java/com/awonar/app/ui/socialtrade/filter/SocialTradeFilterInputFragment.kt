package com.awonar.app.ui.socialtrade.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.constrant.returnData
import com.awonar.app.databinding.AwonarFragmentSocialTradeFilterInputBinding

class SocialTradeFilterInputFragment : Fragment() {

    private val binding: AwonarFragmentSocialTradeFilterInputBinding by lazy {
        AwonarFragmentSocialTradeFilterInputBinding.inflate(layoutInflater)
    }

    private val args: SocialTradeFilterInputFragmentArgs by navArgs()

    private val viewModel: SocialTradeFilterViewModel by activityViewModels()

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
        when (val type = args.filterType) {
            "return" -> viewModel.setFilterList(type,
                "Discover people based on their trading performance.",
                returnData)
            "risk" -> viewModel.setFilterList(type,
                "Risk score is calculated or each user from 1 to 10, where 1 is the lowest possible risk, and 10 is the highest possible risk.",
                returnData)
        }
        binding.awonarSocialTradeFilterSelectorButtonApply.setOnClickListener {
            viewModel.save(args.filterType)
            findNavController().popBackStack()
        }
    }

}