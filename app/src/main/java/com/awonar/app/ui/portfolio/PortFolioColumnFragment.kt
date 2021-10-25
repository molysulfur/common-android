package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnActivedBinding
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnListBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PortFolioColumnFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioColumnListBinding by lazy {
        AwonarFragmentPortfolioColumnListBinding.inflate(layoutInflater)
    }

    private val args: PortFolioColumnFragmentArgs by navArgs()
    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateActivedColumn.collect { newColumn ->
                args.activedColumnName.let { oldColumn ->
                    viewModel.replaceActivedColumn(oldColumn = oldColumn, newColumn = newColumn)
                    findNavController().popBackStack()
                }
            }
        }
    }

}