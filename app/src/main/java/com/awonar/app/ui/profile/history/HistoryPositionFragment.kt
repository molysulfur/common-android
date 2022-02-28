package com.awonar.app.ui.profile.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentHistoryPortfolioBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.profile.user.PublicPortfolioFragment
import com.awonar.app.ui.profile.user.PublicPortfolioFragmentDirections
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class HistoryPositionFragment : Fragment() {

    companion object {
        private const val HISTORY_KEY = "history"
        private const val PORTFOLIO_KEY = "portfolio"
        private const val DIALOG_TAG = "com.awonar.app.ui.profile.history.dialog.title.type"
    }

    private val binding: AwonarFragmentHistoryPortfolioBinding by lazy {
        AwonarFragmentHistoryPortfolioBinding.inflate(layoutInflater)
    }

    private val menuListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                PORTFOLIO_KEY -> findNavController().navigate(
                    HistoryPositionFragmentDirections.historyPositionFragmentToPublicPortfolioFragment())
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

    private val viewModel: HistoryProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateAction.collect {
                findNavController().navigate(it)
            }
        }
        binding.column1 = "Traders"
        binding.column2 = "Profitable"
        binding.column3 = "P/L(%)"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistoryPosition()
        binding.awoanrPortfolioHistoryButtonType.setOnClickListener {
            dialog.show(parentFragmentManager, DIALOG_TAG)
        }
    }

}