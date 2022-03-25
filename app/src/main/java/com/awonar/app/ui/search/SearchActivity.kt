package com.awonar.app.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.awonar.app.databinding.AwonarActivitySearchBinding
import com.google.android.material.tabs.TabLayout
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

    private val binding: AwonarActivitySearchBinding by lazy {
        AwonarActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchViewModel by viewModels()

    private val queryListener: SearchView.OnQueryTextListener by lazy {
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        with(binding.tabLayout2) {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.filter(tab?.text?.toString()?.lowercase() ?: "")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
        with(binding.textInputLayout) {
            setOnQueryTextListener(queryListener)
            setOnCloseListener {
                onBackPressed()
                true
            }
            requestFocus()
        }

    }
}