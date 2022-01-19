package com.awonar.app.ui.socialtrade.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentSocialTradeFilterSelectorBinding

class SocialTradeFilterSelectorFragment : Fragment() {

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

    }
}