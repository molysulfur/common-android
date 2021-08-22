package com.awonar.app.ui.main

import android.os.Bundle
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityMainBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by lazy {
        AwonarActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.awonarMainToolbarHeader.setNavigationOnClickListener {
            binding.awonarMainDrawerSidebar.open()
        }
    }

}