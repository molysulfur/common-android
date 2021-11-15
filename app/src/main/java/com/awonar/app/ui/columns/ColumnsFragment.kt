package com.awonar.app.ui.columns

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnListBinding
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ColumnsFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioColumnListBinding by lazy {
        AwonarFragmentPortfolioColumnListBinding.inflate(layoutInflater)
    }

    private val viewModel: ColumnsViewModel by activityViewModels()
    private val args: ColumnsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.navigateActivedColumn.collect { newColumn ->
                    args.activedColumnName.let { oldColumn ->
                        viewModel.replaceActivedColumn(oldColumn = oldColumn, newColumn = newColumn)
                        findNavController().popBackStack()
                    }
                }
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistoryColumn()
        binding.awonarPortfolioColumnToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}