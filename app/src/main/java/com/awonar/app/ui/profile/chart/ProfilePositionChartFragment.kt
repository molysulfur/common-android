package com.awonar.app.ui.profile.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentProfilePositionChartBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.profile.user.PublicPortfolioFragment
import com.awonar.app.ui.profile.user.PublicPortfolioFragmentDirections
import com.awonar.app.ui.profile.user.UserPortfolioFragment
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toList
import timber.log.Timber

class ProfilePositionChartFragment : Fragment() {

    companion object {
        private const val HISTORY_KEY = "history"
        private const val PORTFOLIO_KEY = "portfolio"
        private const val DIALOG_TAG = "com.awonar.app.ui.profile.chart.dialog.title.type"

    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: ProfileChartViewModel by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()

    private val binding: AwonarFragmentProfilePositionChartBinding by lazy {
        AwonarFragmentProfilePositionChartBinding.inflate(layoutInflater)
    }

    private val menuListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                HISTORY_KEY -> findNavController().navigate(ProfilePositionChartFragmentDirections.profilePositionChartFragmentToHistoryPositionFragment())
                PORTFOLIO_KEY -> findNavController().navigate(ProfilePositionChartFragmentDirections.profilePositionChartFragmentToPublicPortfolioFragment())
            }
        }
    }

    private val dialog: MenuDialogButtonSheet by lazy {
        MenuDialogButtonSheet.Builder().apply {
            setMenus(arrayListOf(
                MenuDialog(key = PORTFOLIO_KEY, text = "Portfolio"),
                MenuDialog(key = HISTORY_KEY, text = "History")
            ))
            setListener(menuListener)
        }.build()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateAction.collect {
                val index: Int = portfolioViewModel.positionList.value.indexOfLast { item ->
                    if (item is PortfolioItem.PositionItem) {
//                        item.title == it
                    }
                    false
                }
                findNavController().navigate(ProfilePositionChartFragmentDirections.profilePositionChartFragmentToInsidePositionPortfolioFragment(
                    index))
            }
        }
        binding.viewModel = viewModel
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getExposure(userViewModel.userState.value)
        binding.awoanrPortfolioChartButtonType.setOnClickListener {
            dialog.show(parentFragmentManager, DIALOG_TAG)
        }
        binding.awoanrPortfolioChartButtonChart.setOnClickListener {
            findNavController().navigate(ProfilePositionChartFragmentDirections.profilePositionChartFragmentToPublicPortfolioFragment())
        }
    }
}