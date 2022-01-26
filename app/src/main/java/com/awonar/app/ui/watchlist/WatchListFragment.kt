package com.awonar.app.ui.watchlist

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class WatchListFragment : Fragment() {

    companion object {
        private const val DIALOG_MENU = "com.awonar.app.ui.watchlist.dialog.menu"
    }

    private val binding: AwonarFragmentWatchlistBinding by lazy {
        AwonarFragmentWatchlistBinding.inflate(layoutInflater)
    }

    private val viewModel: WatchlistViewModel by activityViewModels()

    private val getContent =
        registerForActivityResult(StartActivityForResult()) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateAction.collect {
                binding.awonarWatchlistFragmentHost
                    .findNavController()
                    .navigate(R.id.watchlistListFragment, bundleOf(
                        "watchlistId" to it
                    ))

            }
        }
        binding.lifecycleOwner = activity
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarWatchlistButtonSetting.setOnClickListener {
            settingDialog.show(parentFragmentManager, DIALOG_MENU)
        }
    }
}