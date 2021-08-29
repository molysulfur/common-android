package com.awonar.app.ui.setting.bank

import android.os.Bundle
import com.awonar.app.databinding.AwonarActivityBankAccountBinding
import com.awonar.app.ui.camera.CameraActivity
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankAccountActivity : BaseActivity() {

    private val binding: AwonarActivityBankAccountBinding by lazy {
        AwonarActivityBankAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.appCompatButton.setOnClickListener {
            openActivity(CameraActivity::class.java)
        }
    }
}