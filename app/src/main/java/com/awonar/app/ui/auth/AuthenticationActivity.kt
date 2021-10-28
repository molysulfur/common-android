package com.awonar.app.ui.auth

import android.os.Bundle
import com.awonar.app.databinding.AwonarActivityAuthBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {
    private val binding: AwonarActivityAuthBinding by lazy {
        AwonarActivityAuthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}