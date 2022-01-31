package com.awonar.app.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistListBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class WatchlistRecentlyFragment : Fragment() {

    companion object {
        private const val DEFAULT_DIALOG = "com.awonar.app.ui.watchlist.dialog.set_default"
        private const val ADD_DIALOG = "com.awonar.app.ui.watchlist.dialog.add_watchlist"
    }

    private val binding: AwonarFragmentWatchlistListBinding by lazy {
        AwonarFragmentWatchlistListBinding.inflate(layoutInflater)
    }
    private val viewModel: WatchlistViewModel by activityViewModels()
    private val args: WatchlistListFragmentArgs by navArgs()

    private val addListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            openActivity(AddWatchlistItemActivity::class.java, bundleOf(
                AddWatchlistItemActivity.EXTRA_TYPE to menu.key,
                AddWatchlistItemActivity.EXTRA_FOLDER to args.watchlistId
            ))
        }
    }

    private val addWatchlistDialog by lazy {
        MenuDialogButtonSheet.Builder()
            .setMenus(arrayListOf(
                MenuDialog(key = "stocks",
                    text = "Stocks"),
                MenuDialog(key = "crypto",
                    text = "Crypto"),
                MenuDialog(key = "currencies",
                    text = "Currencies"),
                MenuDialog(key = "etfs",
                    text = "ETFs"),
                MenuDialog(key = "Commodities",
                    text = "commodities")
            ))
            .setListener(addListener)
            .build()
    }

    private val settingListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                "default" -> {
                    viewModel.setDefault(args.watchlistId)
                }
                "add" -> {
                    addWatchlistDialog.show(childFragmentManager, ADD_DIALOG)
                }
            }
        }
    }

    private val settingDialog by lazy {
        MenuDialogButtonSheet.Builder()
            .setMenus(arrayListOf(
                MenuDialog(key = "add",
                    iconRes = R.drawable.awonar_ic_add_list,
                    text = "Add Watchlist"),
                MenuDialog(key = "default",
                    iconRes = R.drawable.awonar_ic_add_list,
                    text = "Set Default"),
                MenuDialog(key = "delete",
                    iconRes = R.drawable.awonar_ic_add_list,
                    text = "Delete Folder")
            ))
            .setListener(settingListener)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.folders.collect {
                viewModel.getWatchlistRecently()
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.showDialog.collect {
                settingDialog.show(childFragmentManager, DEFAULT_DIALOG)
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setTitle("Recently")
    }
}