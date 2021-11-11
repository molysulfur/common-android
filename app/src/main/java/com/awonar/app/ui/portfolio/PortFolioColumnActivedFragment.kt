package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnActivedBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioColumnActivedFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioColumnActivedBinding by lazy {
        AwonarFragmentPortfolioColumnActivedBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.navigateActivedColumn.collect {
                    val action = PortFolioColumnActivedFragmentDirections
                        .actionPortFolioColumnActivedFragmentToPortFolioColumnFragment(it)
                    findNavController().navigate(action)
                }
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}