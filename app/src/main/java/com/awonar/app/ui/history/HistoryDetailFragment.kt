package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.android.model.history.History
import com.awonar.app.databinding.AwonarFragmentHistoryDetailBinding
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.DateUtils
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class HistoryDetailFragment : Fragment() {

    private val viewModel: HistoryViewModel by activityViewModels()

    private val binding: AwonarFragmentHistoryDetailBinding by lazy {
        AwonarFragmentHistoryDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.historyDetailState.collect {
                setUpView(it)
            }
        }
        binding.viewModel = viewModel
        return binding.root
    }

    private fun setUpView(history: History?) {
        history?.let { history ->
            ImageUtil.loadImage(
                binding.awoanrHistoryDetailImageLogo, history.position?.instrument?.logo
            )
            binding.awonarHistoryDetailTextTitle.text =
                "${if (history.position?.isBuy == true) "BUY" else "SELL"} ${history.position?.instrument?.symbol}"
            binding.awonarHistoryDetailTextInvested.text = "$%.2f".format(history.amount)
            binding.awonarHistoryDetailTextUnits.text =
                "%s".format(history.position?.units)
            binding.awonarHistoryDetailTextLeverage.text = "X%s".format(history.position?.leverage)
            binding.awonarHistoryDetailTextOpen.text = "%s".format(history.position?.openRate)
            binding.awonarHistoryDetailTextOpenDescription.text =
                DateUtils.getDate(history.position?.openDateTime)
            binding.awonarHistoryDetailTextClose.text = "%s".format(history.position?.closeRate)
            binding.awonarHistoryDetailTextCloseDescription.text =
                DateUtils.getDate(history.position?.closeDateTime)
            history.position?.netProfit?.let {
                binding.awonarHistoryDetailTextPl.text = "$%.2f".format(history.position?.netProfit)
                binding.awonarHistoryDetailTextPl.setTextColor(
                    ColorChangingUtil.getTextColorChange(
                        requireContext(),
                        it
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarHistoryToolbarHistoryDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.awonarHistoryDetailButtonGotIt.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}