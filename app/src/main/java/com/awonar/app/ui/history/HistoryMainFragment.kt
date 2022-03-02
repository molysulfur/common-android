package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentHistoryMainBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.history.adapter.HistoryAdapter
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class HistoryMainFragment : Fragment() {

    private val viewModel: HistoryViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val binding: AwonarFragmentHistoryMainBinding by lazy {
        AwonarFragmentHistoryMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateAction.collect {
                findNavController().navigate(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.aggregateState.collect { aggregate ->
                aggregate?.let {
                    binding.cashFlows = "$%.2f".format(
                        it.totalMoneyIn.plus(it.totalMoneyOut).minus(it.totalWithdrawalFees)
                    )
                    binding.totalFees = "$%.2f".format(it.totalFees)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            columnsViewModel.activedColumnState.collect {
                if (it.size >= 4) {
                    (binding.awonarHistoryRecyclerItems.adapter as? HistoryAdapter)?.columns =
                        it
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.historiesState.collect {
                if (binding.awonarHistoryRecyclerItems.adapter == null) {
                    binding.awonarHistoryRecyclerItems.apply {
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = HistoryAdapter().apply {
                            onClick = { history ->
                                viewModel.navgiationTo(HistoryMainFragmentDirections.actionHistoryFragmentToHistoryInsideFragment(
                                    history.position?.instrument?.symbol,
                                    history.masterId,
                                    viewModel.timeStamp.value
                                ))
                            }
                            onLoad = {
                                viewModel.getHistory()
                            }
                        }
                    }
                }
                (binding.awonarHistoryRecyclerItems.adapter as HistoryAdapter).itemLists =
                    it.toMutableList()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarHistoryCashContainer.setOnClickListener {
            openActivity(HistoryCashFlowActivity::class.java)
        }
    }
}