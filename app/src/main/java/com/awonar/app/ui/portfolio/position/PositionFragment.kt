package com.awonar.app.ui.portfolio.position

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPositionBinding
import com.awonar.app.ui.columns.ColumnsActivedActivity
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PositionFragment : Fragment() {

    private var positionType: String = "market"

    private val binding: AwonarFragmentPositionBinding by lazy {
        AwonarFragmentPositionBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()
    private val columnViewModel: ColumnsViewModel by activityViewModels()

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                columnViewModel.getActivedColumns()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.columnViewModel = columnViewModel
        binding.market = marketViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        columnViewModel.setColumnType(positionType)
        binding.awonarPortfolioImageIconSetting.setOnClickListener {
            openActivityCompatForResult(activityResult,
                ColumnsActivedActivity::class.java,
                bundleOf(ColumnsActivedActivity.EXTRA_COLUMNS_ACTIVED to positionType))
        }
        binding.awonarPortfolioImageChangeStyle.setOnClickListener {
            navigate()
            positionType = when (positionType) {
                "market" -> "manual"
                "manual" -> "chart"
                "chart" -> "card"
                "card" -> "market"
                else -> "market"
            }
            when (positionType) {
                "market" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list)
                "manual" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_chart)
                "chart" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_card_list)
                "card" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list_2)
            }
        }
    }

    private fun navigate() {
        val direction: NavDirections = when (positionType) {
            "market" -> PositionMarketFragmentDirections.actionPositionMarketFragmentToPositionManualFragment()
            "manual" -> PositionManualFragmentDirections.actionPositionManualFragmentToPositionChartFragment()
            "chart" -> PositionChartFragmentDirections.actionPositionChartFragmentToPositionCardFragment()
            "card" -> PositionCardFragmentDirections.actionPositionCardFragmentToPositionMarketFragment()
            else -> PositionMarketFragmentDirections.actionPositionMarketFragmentToPositionManualFragment()
        }
        Navigation.findNavController(binding.awonarMainDrawerNavigationHostPosition)
            .navigate(direction)
    }
}