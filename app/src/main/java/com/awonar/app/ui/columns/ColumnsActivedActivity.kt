package com.awonar.app.ui.columns

import android.os.Bundle
import androidx.activity.viewModels
import com.awonar.app.databinding.AwonarActivityPortfolioColumnActivedBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColumnsActivedActivity : BaseActivity() {

    private val viewModel: ColumnsViewModel by viewModels()

    companion object {
        const val EXTRA_COLUMNS_ACTIVED =
            "com.awonar.app.ui.columns.extra.columns.actived_type"
    }

    private val binding: AwonarActivityPortfolioColumnActivedBinding by lazy {
        AwonarActivityPortfolioColumnActivedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        intent.extras?.getString(EXTRA_COLUMNS_ACTIVED)?.let {
            viewModel.setColumnType(it)
        }
    }
}