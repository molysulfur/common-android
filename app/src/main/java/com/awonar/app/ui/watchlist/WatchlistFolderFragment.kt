package com.awonar.app.ui.watchlist

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistFolderBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle

class WatchlistFolderFragment : Fragment() {

    companion object {
        private const val FOLDER_DIALOG_TAG = "com.awonar.app.ui.watchlist.dialog.folder.tag"
    }

    private val binding: AwonarFragmentWatchlistFolderBinding by lazy {
        AwonarFragmentWatchlistFolderBinding.inflate(layoutInflater)
        AwonarFragmentWatchlistFolderBinding.inflate(layoutInflater)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.refresh(false)
            }
        }

    private val dialogListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            openActivityCompatForResult(getContent, AddWatchlistFolderActivity::class.java)
        }
    }

    private val settingDialog by lazy {
        MenuDialogButtonSheet.Builder()
            .setMenus(arrayListOf(MenuDialog(key = "add",
                iconRes = R.drawable.awonar_ic_add_list,
                text = "Create")))
            .setListener(dialogListener)
            .build()
    }

    private val viewModel: WatchlistViewModel by activityViewModels()

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
        launchAndRepeatWithViewLifecycle {
            viewModel.showDialog.collect {
                settingDialog.show(childFragmentManager, FOLDER_DIALOG_TAG)
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setTitle("Watchlist")
    }
}