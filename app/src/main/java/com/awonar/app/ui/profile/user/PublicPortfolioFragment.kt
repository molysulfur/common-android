package com.awonar.app.ui.profile.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentPortfolioPublicBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PublicPortfolioFragment : Fragment() {

    companion object {
        private const val HISTORY_KEY = "history"
        private const val PORTFOLIO_KEY = "portfolio"
        private const val DIALOG_TAG = "com.awonar.app.ui.profile.user.dialog.title.type"

        fun newInstance(): UserPortfolioFragment = UserPortfolioFragment()
    }

    private val binding: AwonarFragmentPortfolioPublicBinding by lazy {
        AwonarFragmentPortfolioPublicBinding.inflate(layoutInflater)
    }

    private val menuListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                HISTORY_KEY -> findNavController().navigate(PublicPortfolioFragmentDirections.publicPortfolioFragmentToHistoryPositionFragment())
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

    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateActions.collect {
                findNavController().navigate(it)
            }
        }
        binding.column1 = "Buy/Sell"
        binding.column2 = "Invested(%)"
        binding.column3 = "P/L(%)"
        binding.column4 = "Value(%)"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getManual(userViewModel.userState.value?.username)
        binding.awoanrPortfolioUserButtonType.setOnClickListener {
            dialog.show(parentFragmentManager, DIALOG_TAG)
        }
        binding.awoanrPortfolioUserButtonChart.setOnClickListener {
            findNavController().navigate(PublicPortfolioFragmentDirections.publicPortfolioFragmentToProfilePositionChartFragment())
        }
    }
}