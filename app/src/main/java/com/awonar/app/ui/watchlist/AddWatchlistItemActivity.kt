package com.awonar.app.ui.watchlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarActivityWatchlistAddItemBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.watchlist.adapter.WatchlistAdapter
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddWatchlistItemActivity : BaseActivity() {

    companion object {
        const val EXTRA_FOLDER = "com.awonar.app.ui.watchlist.add_item.extra.folder_id"
        const val EXTRA_TYPE = "com.awonar.app.ui.watchlist.add_item.extra.type"
    }

    private val viewModel: WatchlistViewModel by viewModels()
    private val marketViewModel: MarketViewModel by viewModels()

    private var folderId: String? = null
    private var addType: String? = null

    private val binding: AwonarActivityWatchlistAddItemBinding by lazy {
        AwonarActivityWatchlistAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        intent.extras?.let {
            folderId = it.getString(EXTRA_FOLDER)
            addType = it.getString(EXTRA_TYPE)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.successState.collect {
                    val instrumentList = marketViewModel.instruments.value
                    val instruments =
                        instrumentList.filter { it.categories.contains(addType?.lowercase()) }
                    viewModel.convertItemsWithInstrumentExsit(instruments, folderId)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchlist.collect { itemList ->
                    with(binding.awonarWatchlistAddItemRecyclerList) {
                        if (adapter == null) {
                            layoutManager =
                                LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false)
                            adapter = WatchlistAdapter().apply {
                                onToggleWatchlistInstrument = { id, isSelected ->
                                    if (isSelected) {
                                        viewModel.addItem(id, folderId)
                                    } else {
                                        viewModel.removeItem(id, folderId)
                                    }
                                }
                            }
                        }
                        with(adapter as WatchlistAdapter) {
                            this.itemList = itemList
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                marketViewModel.instruments.collect { instrumentList ->
                    val instruments =
                        instrumentList.filter { it.categories.contains(addType?.lowercase()) }
                    viewModel.convertItemsWithInstrumentExsit(instruments, folderId)
                }
            }
        }
    }


}