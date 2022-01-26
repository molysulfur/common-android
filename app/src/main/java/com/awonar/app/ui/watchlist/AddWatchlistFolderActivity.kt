package com.awonar.app.ui.watchlist

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWatchlistAddBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddWatchlistFolderActivity : BaseActivity() {

    private val viewModel: WatchlistViewModel by viewModels()

    private val binding: AwonarFragmentWatchlistAddBinding by lazy {
        AwonarFragmentWatchlistAddBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finished.collect {
                    setResult(Activity.RESULT_OK)
                    onBackPressed()
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collect {
                    binding.awonarAddWatchlistInputName.error = it
                }
            }
        }
        binding.awonarAddWatchlistButtonAdd.setOnClickListener {
            if (!binding.awonarAddWatchlistInputName.editText?.text.isNullOrEmpty()) {
                viewModel.addFolder(binding.awonarAddWatchlistInputName.editText?.text.toString())
            } else {
                binding.awonarAddWatchlistInputName.error = getString(R.string.awonar_text_required)
            }
        }
        binding.awonarAddWatchlistButtonClose.setOnClickListener {
            onBackPressed()
        }
    }
}