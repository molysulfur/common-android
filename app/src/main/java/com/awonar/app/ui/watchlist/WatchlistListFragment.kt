package com.awonar.app.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistListBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class WatchlistListFragment : Fragment() {

    private val binding: AwonarFragmentWatchlistListBinding by lazy {
        AwonarFragmentWatchlistListBinding.inflate(layoutInflater)
    }
    private val viewModel: WatchlistViewModel by activityViewModels()
    private val args: WatchlistListFragmentArgs by navArgs()


    private val dialogListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                "default" -> {
                    viewModel.setDefault(args.watchlistId)
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
            .setListener(dialogListener)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.showDialog.collect {
                settingDialog.show(childFragmentManager, "sad")
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWatchlist(args.watchlistId)
    }
}