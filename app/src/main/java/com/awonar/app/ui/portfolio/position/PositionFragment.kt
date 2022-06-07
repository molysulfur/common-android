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
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsActivedActivity
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.chart.PositionChartFragmentDirections
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class PositionFragment : Fragment() {

    companion object {
        private const val SECTION_DIALOG = "com.awonar.app.ui.portfolio.position.dialog.section"
        private const val KEY_DIALOG_HISTORY =
            "com.awonar.app.ui.portfolio.position.dialog.key_history"
        private const val KEY_DIALOG_ORDER = "com.awonar.app.ui.portfolio.position.dialog.key_order"
    }


    private val binding: AwonarFragmentPositionBinding by lazy {
        AwonarFragmentPositionBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val columnViewModel: ColumnsViewModel by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()
    private val positionViewModel: PositionViewModel by activityViewModels()

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                columnViewModel.getActivedColumns()
            }
        }

    private val listenerDialog = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                KEY_DIALOG_HISTORY -> findNavController().navigate(PositionFragmentDirections.actionPositionFragmentToHistoryFragment())
                KEY_DIALOG_ORDER -> {}
            }
        }

    }

    private val titleDialog: MenuDialogButtonSheet by lazy {
        val menus = arrayListOf(
            MenuDialog(
                key = KEY_DIALOG_HISTORY,
                text = "History"
            ),
            MenuDialog(
                key = KEY_DIALOG_ORDER,
                text = "Orders"
            )
        )
        MenuDialogButtonSheet.Builder()
            .setMenus(menus)
            .setListener(listenerDialog)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            positionViewModel.styleTypeState.collectLatest { type ->
                columnViewModel.setColumnType(type)
                setIconStyle(type)
            }
        }
        launchAndRepeatWithViewLifecycle {
            portfolioViewModel.positionState.collectIndexed { _, value ->
                positionViewModel.getPositionListItems(value)
            }
        }
        launchAndRepeatWithViewLifecycle {
            columnViewModel.visibleState.collect {
                binding.awonarPortfolioLayoutColumns.visibility = it
            }
        }
        binding.columnViewModel = columnViewModel
        binding.positionViewModel = positionViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setIconStyle(type: String) {
        when (type) {
            "market" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list)
            "manual" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_chart)
            "chart" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_card_list)
            "card" -> binding.awonarPortfolioImageChangeStyle.setImageResource(R.drawable.awonar_ic_list_2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarPortfolioTextTitleSection.setOnClickListener {
            titleDialog.show(parentFragmentManager, SECTION_DIALOG)
        }
        binding.awonarPortfolioImageIconSetting.setOnClickListener {
            openActivityCompatForResult(
                activityResult,
                ColumnsActivedActivity::class.java,
                bundleOf(ColumnsActivedActivity.EXTRA_COLUMNS_ACTIVED to positionViewModel.styleTypeState.value)
            )
        }
        binding.awonarPortfolioImageChangeStyle.setOnClickListener {
            positionViewModel.toggleStyle()

        }
    }

    private fun navigate(type: String) {
        val direction: NavDirections = when (type) {
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