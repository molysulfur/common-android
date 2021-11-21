package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentHistoryInsideInstrumentBinding
import com.awonar.app.databinding.AwonarFragmentHistoryInstideLevelTwoBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class HistoryInsideLevelTwoFragment : Fragment() {

    private val viewModel: HistoryInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val args: HistoryInsideFragmentArgs by navArgs()

    private val binding: AwonarFragmentHistoryInstideLevelTwoBinding by lazy {
        AwonarFragmentHistoryInstideLevelTwoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {

        }
        launchAndRepeatWithViewLifecycle {

        }
        binding.columnsViewModel = columnsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.symbol?.let {
            viewModel.getArgreation(it, args.timestamp)
            viewModel.getHistoryCopies(it, args.timestamp)
        }
        args.copy?.let {
            viewModel.getCopiesHistory(it, args.timestamp)
        }

    }
}
