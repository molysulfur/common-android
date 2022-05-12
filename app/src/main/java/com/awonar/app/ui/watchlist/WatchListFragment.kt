package com.awonar.app.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderDialog
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WatchListFragment : Fragment() {

    private val binding: AwonarFragmentWatchlistBinding by lazy {
        AwonarFragmentWatchlistBinding.inflate(layoutInflater)
    }

    private val viewModel: WatchlistViewModel by activityViewModels()
    private val portViewModel: PortFolioViewModel by activityViewModels()


    private val dialogListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                "default" -> {
                    binding.awonarWatchlistFragmentHost
                        .findNavController()
                        .navigate(R.id.watchlistDefaultFragment)
                }
                "recently" -> {
                    binding.awonarWatchlistFragmentHost
                        .findNavController()
                        .navigate(R.id.watchlistRecentlyFragment)
                }
                "all" -> {
                    binding.awonarWatchlistFragmentHost
                        .findNavController()
                        .navigate(R.id.watchlistFolderFragment)
                }
            }
        }
    }

    private lateinit var settingDialog: MenuDialogButtonSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.showOpenTrade.collectLatest {
                OrderDialog.Builder()
                    .setType(it.second)
                    .setSymbol(it.first)
                    .build()
                    .show(childFragmentManager)
            }
        }
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                portViewModel.sumTotalProfitAndEquity(quotes)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.errorState.collect {
                toast(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.title.collect { title ->
                binding.sector = title
            }
        }
        binding.portViewModel = portViewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarWatchlistButtonSector.setOnClickListener {
            val folders = viewModel.folders.value
            settingDialog = MenuDialogButtonSheet.Builder()
                .setMenus(
                    arrayListOf(
                        MenuDialog(key = "default",
                            iconRes = R.drawable.awonar_ic_add_list,
                            text = folders.find { it.default }?.name
                        ),
                        MenuDialog(key = "recently",
                            iconRes = R.drawable.awonar_ic_add_list,
                            text = folders.find { it.static }?.name
                        ),
                        MenuDialog(
                            key = "all",
                            iconRes = R.drawable.awonar_ic_add_list,
                            text = "Watchlist"
                        )
                    )
                )
                .setListener(dialogListener)
                .build()
            settingDialog.show(childFragmentManager, "Title")
        }
        binding.awonarWatchlistButtonSetting.setOnClickListener {
            viewModel.show()
        }
    }
}