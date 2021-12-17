package com.awonar.app.ui.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.databinding.AwonarActivityHistoryCashflowBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryCashFlowActivity : BaseActivity() {

    private val binding: AwonarActivityHistoryCashflowBinding by lazy {
        AwonarActivityHistoryCashflowBinding.inflate(layoutInflater)
    }

    private val viewModel: HistoryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getCashflow()

    }
}